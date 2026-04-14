package com.example.voltwatch.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BatteryEntity::class], version = 1)
abstract class BatteryDatabase : RoomDatabase() {

    abstract fun batteryDao(): BatteryDao

    companion object {
        @Volatile
        private var INSTANCE: BatteryDatabase? = null

        fun getInstance(context: Context): BatteryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BatteryDatabase::class.java,
                    "battery_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
