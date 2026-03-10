package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.itemapi.HistorialPesoAPI
import com.example.myapplication.model.HistorialPeso
import com.example.myapplication.service.HistorialPesoService
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    private val _listaHistorial = MutableLiveData<List<HistorialPeso>>()
    val listaHistorial: LiveData<List<HistorialPeso>> = _listaHistorial

    // Guardamos una copia de la lista completa para poder filtrar sin volver a llamar a la API
    private var listaCompletaCache: List<HistorialPeso> = emptyList()

    private val apiService = HistorialPesoAPI

    // ERRORRRRRRRRRRRRRRR
    fun obtenerHistorial() {
        viewModelScope.launch {
            try {
                val response = apiService.findAll()
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    listaCompletaCache = lista
                    _listaHistorial.value = lista
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    // Llama al POST de Retrofit que configuramos antes
    fun crearNuevoPeso(fecha: String, peso: Int, idUser: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.create(fecha, peso, idUser)
                if (response.isSuccessful) {
                    // Si se creó con éxito, volvemos a pedir la lista actualizada al servidor
                    obtenerHistorial()
                }
            } catch (e: Exception) {
                // Manejar error
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