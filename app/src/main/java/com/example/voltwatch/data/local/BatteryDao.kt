package com.example.voltwatch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BatteryDao {

    @Insert
    suspend fun insert(entity: BatteryEntity)

    @Query("SELECT * FROM battery_logs ORDER BY timestamp DESC")
    fun getLogs(): Flow<List<BatteryEntity>>
}