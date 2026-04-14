package com.example.voltwatch.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.*
import com.example.voltwatch.data.local.BatteryDatabase
import com.example.voltwatch.data.local.BatteryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class BatteryInfo(
    val level: Int,
    val temp: Float,
    val voltage: Int,
    val tech: String,
    val status: String,
    val remainingTime: String
)

class BatteryReceiver(
    private val onUpdate: (BatteryInfo) -> Unit
) : BroadcastReceiver() {

    private var lastSavedLevel = -1
    
    companion object {
        private var wasCharging = false
    }

    override fun onReceive(context: Context, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f
        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        val tech = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"

        // ⚡ Get Status
        val statusInt = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = statusInt == BatteryManager.BATTERY_STATUS_CHARGING ||
                statusInt == BatteryManager.BATTERY_STATUS_FULL

        // 📳 Plug-in Feedback (Vibration)
        if (isCharging && !wasCharging) {
            triggerVibration(context)
        }
        wasCharging = isCharging

        val status = when (statusInt) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging ⚡"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging 🔋"
            BatteryManager.BATTERY_STATUS_FULL -> "Full 🔋"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging ⚠️"
            else -> "Unknown"
        }

        // ⏳ Estimate Remaining Time to Full Charge
        val remainingTime = if (statusInt == BatteryManager.BATTERY_STATUS_FULL) {
            "Full"
        } else if (isCharging) {
            val manager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val computeTime = manager.computeChargeTimeRemaining()
                if (computeTime > 0) {
                    val hours = computeTime / 3600000
                    val minutes = (computeTime % 3600000) / 60000
                    if (hours > 0) "${hours}h ${minutes}m left" else "${minutes}m left"
                } else {
                    "Calculating..."
                }
            } else {
                "Charging..."
            }
        } else {
            "Not Charging"
        }

        onUpdate(BatteryInfo(level, temp, voltage, tech, status, remainingTime))

        if (level != lastSavedLevel) {
            lastSavedLevel = level
            CoroutineScope(Dispatchers.IO).launch {
                val db = BatteryDatabase.getInstance(context)
                db.batteryDao().insert(
                    BatteryEntity(
                        level = level,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun triggerVibration(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
        }
    }
}
