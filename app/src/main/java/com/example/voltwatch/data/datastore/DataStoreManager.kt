package com.example.voltwatch.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

val TARGET_KEY = intPreferencesKey("target")

class DataStoreManager(private val context: Context) {

    val targetFlow: Flow<Int> = context.dataStore.data.map { pref ->
        pref[TARGET_KEY] ?: -1
    }

    suspend fun saveTarget(value: Int) {
        context.dataStore.edit { pref ->
            pref[TARGET_KEY] = value
        }
    }
}