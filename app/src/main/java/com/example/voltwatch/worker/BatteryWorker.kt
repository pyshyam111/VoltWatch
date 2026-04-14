package com.example.voltwatch.worker

import android.content.Context
import android.os.BatteryManager
import androidx.work.*
import com.example.voltwatch.data.local.*

class BatteryWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val manager =
            applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val level = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        BatteryDatabase.getInstance(applicationContext)
            .batteryDao()
            .insert(BatteryEntity(level = level, timestamp = System.currentTimeMillis()))

        return Result.success()
    }
}
