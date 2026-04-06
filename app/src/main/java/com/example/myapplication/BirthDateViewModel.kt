package com.example.myapplication

import android.R
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData

class BirthDateViewModel : ViewModel() {
    private val _fecha = MutableLiveData<String>()
    val fecha : LiveData<String> = _fecha

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    fun checkData() {
        if (_fecha.value.isNullOrBlank()) {
            _error.value = "EL CAMPO NO PUEDE ESTAR VACIO"
        } else {
            _error.value = "" // O null si cambias el tipo
        }
    }

    fun actualizaFecha(fecha : String){
        this._fecha.value = fecha
        checkData()
    }
}