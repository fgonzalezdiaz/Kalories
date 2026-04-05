package com.example.myapplication.persistence

import com.example.myapplication.UserSession
import com.google.firebase.firestore.FirebaseFirestore

// Sube las estadísticas de uso a Firestore
object FirestoreUsageSync {

    private const val COLLECTION = "kalories_usage_stats"

    private val db: FirebaseFirestore get() = FirebaseFirestore.getInstance()

    suspend fun push(repo: UsageStatsRepository) {
        val userId = UserSession.userId.toString()
        val s = repo.getSnapshot()
        val data = hashMapOf(
            "totalForegroundMs" to s.totalForegroundMs,
            "screenTimeMs" to s.screenTimeMs,
            "screenVisits" to s.screenVisits,
            "historialItemsAdded" to s.historialItemsAdded,
            "historialItemsRemoved" to s.historialItemsRemoved,
        )
        try {
            db.collection(COLLECTION).document(userId).set(data)
        } catch (e: Exception) {
            // sin red, no pasa nada
        }
    }
}
