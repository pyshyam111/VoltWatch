package com.example.voltwatch.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "battery_logs")
data class BatteryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val level: Int,
    val timestamp: Long
)