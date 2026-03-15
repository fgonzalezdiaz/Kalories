package com.example.myapplication.model

import java.math.BigDecimal

data class User(
    val id: Long? = null,
    val email: String,
    val contrasena: String,
    val genero: String? = null,
    val fechaNacimiento: String? = null,
    val altura: Int? = null,
    val peso: Double? = null,
    val nivelActividad: Int? = null,
    val objetivo: Int? = null,
    val imgPath: String? = null
)
