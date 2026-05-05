package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignInControl : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password
    private val _password2 = MutableLiveData<String>()
    val password2: LiveData<String> = _password2
    private val _fechaNacimiento = MutableLiveData<String>()
    val fechaNacimiento: LiveData<String> = _fechaNacimiento
    private val _errorEmail = MutableLiveData<String?>()
    val errorEmail: LiveData<String?> = _errorEmail
    private val _errorPassword = MutableLiveData<String?>()
    val errorPassword: LiveData<String?> = _errorPassword
    private val _errorPassword2 = MutableLiveData<String?>()
    val errorPassword2: LiveData<String?> = _errorPassword2
    private val _errorFechaNacimiento = MutableLiveData<String?>()
    val errorFechaNacimiento: LiveData<String?> = _errorFechaNacimiento

    fun actualizaEmail(email: String) {
        _email.value = email
        checkEmail()
    }

    fun actualizaPassword(password: String) {
        _password.value = password
        checkPassword()
    }

    fun actualizaPassword2(password2: String) {
        _password2.value = password2
        checkPassword2()
    }

    fun actualizaFechaNacimiento(fecha: String) {
        _fechaNacimiento.value = fecha
        checkFechaNacimiento()
    }

    fun checkEmail() {
        val email = _email.value
        if (email == null || email.isEmpty()) {
            _errorEmail.value = "El email no puede estar vacío"
        } else if (!EMAIL_REGEX.matches(email)) {
            _errorEmail.value = "El email no es válido"
        } else if (email.length > 95) {
            _errorEmail.value = "El email es demasiado largo"
        } else {
            _errorEmail.value = null
        }
    }

    fun checkPassword() {
        val password = _password.value
        if (password == null || password.isEmpty()) {
            _errorPassword.value = "La contraseña no puede estar vacía"
        } else if (password.length < 8) {
            _errorPassword.value = "La contraseña debe tener al menos 8 caracteres"
        } else if (!password.any { it.isUpperCase() }) {
            _errorPassword.value = "La contraseña debe tener al menos una mayúscula"
        } else if (!password.any { it.isDigit() }) {
            _errorPassword.value = "La contraseña debe tener al menos un número"
        } else if (!password.any { it in "!@#\$%^&*()_+-=[]{}|;:,.<>?" }) {
            _errorPassword.value = "La contraseña debe tener al menos un carácter especial"
        } else {
            _errorPassword.value = null
        }
    }

    fun checkPassword2() {
        val password2 = _password2.value
        if (password2 == null || password2.isEmpty()) {
            _errorPassword2.value = "La contraseña no puede estar vacía"
        } else if (password2 != _password.value) {
            _errorPassword2.value = "Las contraseñas no coinciden"
        } else {
            _errorPassword2.value = null
        }
    }

    fun checkFechaNacimiento() {
        val fecha = _fechaNacimiento.value
        if (fecha == null || fecha.isEmpty()) {
            _errorFechaNacimiento.value = "La fecha no puede estar vacía"
            return
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.isLenient = false
        val date = try {
            sdf.parse(fecha)
        } catch (e: Exception) {
            null
        }
        if (date == null) {
            _errorFechaNacimiento.value = "Formato de fecha inválido"
            return
        }
        val today = Calendar.getInstance()
        val birthDate = Calendar.getInstance()
        birthDate.time = date
        if (birthDate.after(today)) {
            _errorFechaNacimiento.value = "La fecha no es válida"
            return
        }
        val yearsAge = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        val monthDiff = today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH)
        val dayDiff = today.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH)
        val age = if (monthDiff < 0 || (monthDiff == 0 && dayDiff < 0)) yearsAge - 1 else yearsAge
        if (age < 18) {
            _errorFechaNacimiento.value = "Debes ser mayor de 18 años"
        } else if (age > 99) {
            _errorFechaNacimiento.value = "Límite de edad superado"
        } else {
            _errorFechaNacimiento.value = null
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    }
}
