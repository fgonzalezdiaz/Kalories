package com.example.myapplication

object UserSession {
    var userId: Long = 1L
    var email: String? = null
    
    fun logout() {
        userId = 1L
        email = null
    }
}
