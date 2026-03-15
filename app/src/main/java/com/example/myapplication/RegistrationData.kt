package com.example.myapplication

import com.example.myapplication.model.User

object RegistrationData {
    var email: String? = null
    var contrasena: String? = null
    var genero: String? = null
    var fechaNacimiento: String? = null
    var altura: Int? = null
    var peso: Double? = null
    var nivelActividad: Int? = null
    var objetivo: Int? = null

    fun toUser(): User {
        return User(
            email = email ?: "",
            contrasena = contrasena ?: "",
            genero = genero,
            fechaNacimiento = fechaNacimiento,
            altura = altura,
            peso = peso,
            nivelActividad = nivelActividad,
            objetivo = objetivo
        )
    }

    fun clear() {
        email = null
        contrasena = null
        genero = null
        fechaNacimiento = null
        altura = null
        peso = null
        nivelActividad = null
        objetivo = null
    }
}
