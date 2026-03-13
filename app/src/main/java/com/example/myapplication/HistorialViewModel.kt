package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.itemapi.HistorialPesoAPI
import com.example.myapplication.model.HistorialPeso
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    private val _listaHistorial = MutableLiveData<List<HistorialPeso>>()
    val listaHistorial: LiveData<List<HistorialPeso>> = _listaHistorial

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Guardamos una copia de la lista completa para poder filtrar sin volver a llamar a la API
    private var listaCompletaCache: List<HistorialPeso> = emptyList()

    private val apiService = HistorialPesoAPI.API()

    fun obtenerHistorial() {
        viewModelScope.launch {
            try {
                val response = apiService.findAll()
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    listaCompletaCache = lista
                    _listaHistorial.value = lista
                    _errorMessage.value = null
                } else {
                    val errorMsg = "Error al obtener historial: ${response.code()}"
                    Log.e("HistorialViewModel", errorMsg)
                    _errorMessage.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error de red: ${e.message}"
                Log.e("HistorialViewModel", errorMsg, e)
                _errorMessage.value = errorMsg
            }
        }
    }

    // Llama al POST de Retrofit que configuramos antes
    fun crearNuevoPeso(fecha: String, peso: Int, idUser: Long) {
        viewModelScope.launch {
            try {
                Log.d("HistorialViewModel", "Enviando peso: fecha=$fecha, peso=$peso, idUser=$idUser")
                val response = apiService.create(fecha, peso, idUser)
                if (response.isSuccessful) {
                    Log.d("HistorialViewModel", "Peso creado con éxito")
                    _errorMessage.value = "Peso guardado correctamente"
                    // Si se creó con éxito, volvemos a pedir la lista actualizada al servidor
                    obtenerHistorial()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin cuerpo de error"
                    val errorMsg = "Error al guardar (Código ${response.code()}): $errorBody"
                    Log.e("HistorialViewModel", errorMsg)
                    _errorMessage.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error al conectar con Oracle: ${e.message}"
                Log.e("HistorialViewModel", errorMsg, e)
                _errorMessage.value = errorMsg
            }
        }
    }

    // Replica exactamente la lógica de tu filtro original pero sobre los objetos de la BD
    fun filtrarLista(filtro: String) {
        if (filtro.isBlank()) {
            // Si el filtro está vacío, mostramos todo
            _listaHistorial.value = listaCompletaCache
        } else {
            // Filtramos si la fecha o el peso contienen el texto escrito
            val listaFiltrada = listaCompletaCache.filter { item ->
                item.fecha.contains(filtro, ignoreCase = true) ||
                        item.peso.toString().contains(filtro)
            }
            _listaHistorial.value = listaFiltrada
        }
    }
}