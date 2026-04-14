package com.example.voltwatch.data.repository

import com.example.voltwatch.data.local.BatteryDao
import com.example.voltwatch.data.local.BatteryEntity
import kotlinx.coroutines.flow.Flow

class BatteryRepository(private val dao: BatteryDao) {

    // 🔋 Insert battery log
    suspend fun insert(level: Int) {
        val entity = BatteryEntity(
            level = level,
            timestamp = System.currentTimeMillis()
        )
        dao.insert(entity)
    }

    // 📊 Get all logs (Flow)
    fun getLogs(): Flow<List<BatteryEntity>> {
        return dao.getLogs()
    }
}