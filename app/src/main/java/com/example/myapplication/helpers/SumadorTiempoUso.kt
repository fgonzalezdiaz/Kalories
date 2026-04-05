package com.example.myapplication.helpers

import android.app.Activity
import android.app.Application
import android.os.SystemClock
import com.example.myapplication.persistence.FirestoreUsageSync
import com.example.myapplication.persistence.UsageStatsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Guarda el tiempo que el usuario pasa en cada pantalla
object SumadorTiempoUso {

    var repository: UsageStatsRepository? = null

    private val scope = CoroutineScope(Dispatchers.IO)
    private var resumeAt: Long = 0L
    private var pantallaActual: String? = null

    fun init(application: Application) {
        if (repository == null) {
            repository = UsageStatsRepository(application.applicationContext)
        }
    }

    fun onActivityResumed(activity: Activity) {
        if (repository == null) return
        val pantalla = activity.javaClass.simpleName
        pantallaActual = pantalla
        resumeAt = SystemClock.elapsedRealtime()
        scope.launch {
            try {
                repository?.incrementScreenVisit(pantalla)
            } catch (e: Exception) { }
        }
    }

    fun onActivityPaused(activity: Activity) {
        if (repository == null) return
        val pantalla = activity.javaClass.simpleName
        if (pantalla != pantallaActual) return
        val tiempo = SystemClock.elapsedRealtime() - resumeAt
        pantallaActual = null
        if (tiempo <= 0L) return
        scope.launch {
            try {
                repository?.addScreenForegroundTime(pantalla, tiempo)
                repository?.let { FirestoreUsageSync.push(it) }
            } catch (e: Exception) { }
        }
    }

    fun recordHistorialItemAdded() {
        if (repository == null) return
        scope.launch {
            try { repository?.incrementHistorialAdded() } catch (e: Exception) { }
        }
    }

    fun recordHistorialItemRemoved() {
        if (repository == null) return
        scope.launch {
            try { repository?.incrementHistorialRemoved() } catch (e: Exception) { }
        }
    }
}
