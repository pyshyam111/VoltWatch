package com.example.voltwatch.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.voltwatch.data.datastore.DataStoreManager
import com.example.voltwatch.receiver.BatteryInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.voltwatch.data.local.BatteryDatabase
import com.example.voltwatch.data.local.BatteryEntity
import com.example.voltwatch.utils.BatteryAI
import com.example.voltwatch.utils.NotificationHelper

class BatteryViewModel(application: Application) : AndroidViewModel(application) {

    private val db = BatteryDatabase.getInstance(application)
    private val dao = db.batteryDao()

    private val dataStoreManager = DataStoreManager(application)

    private val _battery = MutableStateFlow(
        BatteryInfo(85, 35.5f, 4, "Li-ion", "Normal", "Calculating...")
    )
    val battery: StateFlow<BatteryInfo> = _battery


    val batteryHistory: StateFlow<List<BatteryEntity>> =
        dao.getLogs()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val target: StateFlow<Int> = dataStoreManager.targetFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        -1
    )

    fun updateBattery(info: BatteryInfo) {
        _battery.value = info
    }

    fun saveTarget(value: Int) {
        viewModelScope.launch {
            dataStoreManager.saveTarget(value)
            alertTriggered = false // Reset alert flag for new target
        }
    }

    private var alertTriggered = false

    fun checkAndTriggerAlert(context: Context, currentLevel: Int) {
        val targetValue = target.value

        if (targetValue == -1) return

        // 🔥 LOGS FOR DEBUGGING (Check Logcat for "BATTERY_ALERT")
        android.util.Log.d("BATTERY_ALERT", "Checking: Current=$currentLevel, Target=$targetValue, Triggered=$alertTriggered")

        // Reset if battery is charging above target
        if (currentLevel > targetValue) {
            alertTriggered = false
        }

        // Trigger Alert
        if (currentLevel <= targetValue && !alertTriggered) {
            alertTriggered = true
            android.util.Log.d("BATTERY_ALERT", "TRINGGERING NOTIFICATION! 🔔")
            NotificationHelper.showNotification(
                context,
                "Battery Alert 🔔",
                "Your battery has reached $currentLevel% (Target: $targetValue%)"
            )
        }
    }

    val batteryInsights: StateFlow<List<String>> =
        batteryHistory.map { history ->
            BatteryAI.generateInsights(history)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}
