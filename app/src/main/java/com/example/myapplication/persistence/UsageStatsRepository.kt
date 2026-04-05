package com.example.myapplication.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.usageDataStore: DataStore<Preferences> by preferencesDataStore(name = "kalories_usage_stats")

data class UsageSnapshot(
    val totalForegroundMs: Long,
    val screenTimeMs: Map<String, Long>,
    val screenVisits: Map<String, Long>,
    val historialItemsAdded: Int,
    val historialItemsRemoved: Int,
)

class UsageStatsRepository(private val context: Context) {

    private val dataStore = context.usageDataStore

    companion object {
        private val KEY_TOTAL_MS = longPreferencesKey("total_foreground_ms")
        private val KEY_SCREEN_TIMES = stringPreferencesKey("screen_times_encoded")
        private val KEY_SCREEN_VISITS = stringPreferencesKey("screen_visits_encoded")
        private val KEY_HIST_ADDED = intPreferencesKey("historial_items_added")
        private val KEY_HIST_REMOVED = intPreferencesKey("historial_items_removed")
    }

    private val snapshotFlow = dataStore.data.map { prefs ->
        UsageSnapshot(
            totalForegroundMs = prefs[KEY_TOTAL_MS] ?: 0L,
            screenTimeMs = decodeMap(prefs[KEY_SCREEN_TIMES] ?: ""),
            screenVisits = decodeMap(prefs[KEY_SCREEN_VISITS] ?: "").toMap(),
            historialItemsAdded = prefs[KEY_HIST_ADDED] ?: 0,
            historialItemsRemoved = prefs[KEY_HIST_REMOVED] ?: 0,
        )
    }

    suspend fun getSnapshot(): UsageSnapshot = snapshotFlow.first()

    suspend fun addScreenForegroundTime(screenKey: String, deltaMs: Long) {
        if (deltaMs <= 0L) return
        dataStore.edit { prefs ->
            prefs[KEY_TOTAL_MS] = (prefs[KEY_TOTAL_MS] ?: 0L) + deltaMs
            val times = decodeMap(prefs[KEY_SCREEN_TIMES] ?: "")
            times[screenKey] = (times[screenKey] ?: 0L) + deltaMs
            prefs[KEY_SCREEN_TIMES] = encodeMap(times)
        }
    }

    suspend fun incrementScreenVisit(screenKey: String) {
        dataStore.edit { prefs ->
            val visits = decodeMap(prefs[KEY_SCREEN_VISITS] ?: "")
            visits[screenKey] = (visits[screenKey] ?: 0L) + 1L
            prefs[KEY_SCREEN_VISITS] = encodeMap(visits)
        }
    }

    suspend fun incrementHistorialAdded() {
        dataStore.edit { prefs ->
            prefs[KEY_HIST_ADDED] = (prefs[KEY_HIST_ADDED] ?: 0) + 1
        }
    }

    suspend fun incrementHistorialRemoved() {
        dataStore.edit { prefs ->
            prefs[KEY_HIST_REMOVED] = (prefs[KEY_HIST_REMOVED] ?: 0) + 1
        }
    }

    private fun encodeMap(map: Map<String, Long>): String =
        map.entries.joinToString("|") { "${it.key}=${it.value}" }

    private fun decodeMap(encoded: String): MutableMap<String, Long> {
        if (encoded.isBlank()) return mutableMapOf()
        return encoded.split('|').mapNotNull { part ->
            val i = part.indexOf('=')
            if (i <= 0) null
            else {
                val key = part.substring(0, i)
                val value = part.substring(i + 1).toLongOrNull() ?: return@mapNotNull null
                key to value
            }
        }.toMap().toMutableMap()
    }
}
