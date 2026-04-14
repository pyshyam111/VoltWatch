package com.example.voltwatch

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.voltwatch._ui.navigation.NavGraph
import com.example.voltwatch.receiver.BatteryReceiver
import com.example.voltwatch.viewmodel.BatteryViewModel
import com.example.voltwatch.worker.BatteryWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var receiver: BatteryReceiver
    private lateinit var vm: BatteryViewModel

    // ✅ Modern permission launcher
    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                android.util.Log.d("PERMISSION", "Notification Permission Granted ✅")
            } else {
                android.util.Log.d("PERMISSION", "Notification Permission Denied ❌")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(this)[BatteryViewModel::class.java]

        // 🔔 Ask permission
        requestNotificationPermission()

        // 🔋 Receiver
        receiver = BatteryReceiver { info ->

            vm.updateBattery(info)

            // 🔔 Alert trigger
            vm.checkAndTriggerAlert(this, info.level)
        }

        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        setContent {
            MaterialTheme {
                NavGraph(vm)
            }
        }

        // 🔄 Worker (background logging)
        val work = PeriodicWorkRequestBuilder<BatteryWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "battery_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }

    // ✅ Permission function (Modern way)
    private fun requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            unregisterReceiver(receiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}