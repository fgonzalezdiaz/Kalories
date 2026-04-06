package com.example.myapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.helpers.SumadorTiempoUso
import com.example.myapplication.itemapi.HistorialPesoAPI
import com.example.myapplication.model.HistorialPeso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistorialViewModel(application: Application) : AndroidViewModel(application) {

    private val _listaHistorial = MutableLiveData<List<HistorialPeso>>()
    val listaHistorial: LiveData<List<HistorialPeso>> = _listaHistorial

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var listaCompletaCache: List<HistorialPeso> = emptyList()

    private val apiService = HistorialPesoAPI.API()

    fun obtenerHistorial() {
        viewModelScope.launch {
            try {
                // withContext(Dispatchers.IO) mou la crida de xarxa al thread d'IO
                // alliberant el thread principal (UI) mentre espera la resposta
                val response = withContext(Dispatchers.IO) {
                    apiService.findByIdUser(UserSession.userId)
                }
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

    fun crearNuevoPeso(fecha: String, peso: Int, idUser: Long) {
        viewModelScope.launch {
            try {
                Log.d("HistorialViewModel", "Enviando peso: fecha=$fecha, peso=$peso, idUser=$idUser")
                val response = withContext(Dispatchers.IO) {
                    apiService.create(fecha, peso, idUser)
                }
                if (response.isSuccessful) {
                    Log.d("HistorialViewModel", "Peso creado con éxito")
                    _errorMessage.value = "Peso guardado correctament"
                    SumadorTiempoUso.recordHistorialItemAdded()
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

    fun modificarPeso(id: Long, fecha: String, peso: Int) {
        viewModelScope.launch {
            try {
                Log.d("HistorialViewModel", "Modificando peso: id=$id, fecha=$fecha, peso=$peso")
                val response = withContext(Dispatchers.IO) {
                    apiService.update(id, fecha, peso, UserSession.userId)
                }
                if (response.isSuccessful) {
                    Log.d("HistorialViewModel", "Peso modificado con éxito")
                    _errorMessage.value = "Peso modificado correctamente"
                    obtenerHistorial()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin cuerpo de error"
                    val errorMsg = "Error al modificar (Código ${response.code()}): $errorBody"
                    Log.e("HistorialViewModel", errorMsg)
                    _errorMessage.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error al modificar peso: ${e.message}"
                Log.e("HistorialViewModel", errorMsg, e)
                _errorMessage.value = errorMsg
            }
        }
    }

    fun eliminarPeso(historialPeso: HistorialPeso) {
        viewModelScope.launch {
            try {
                Log.d("HistorialViewModel", "Eliminando peso: id=${historialPeso.id}")
                val response = withContext(Dispatchers.IO) {
                    apiService.delete(historialPeso)
                }
                if (response.isSuccessful) {
                    Log.d("HistorialViewModel", "Peso eliminado con éxito")
                    _errorMessage.value = "Peso eliminado correctamente"
                    SumadorTiempoUso.recordHistorialItemRemoved()
                    obtenerHistorial()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin cuerpo de error"
                    val errorMsg = "Error al eliminar (Código ${response.code()}): $errorBody"
                    Log.e("HistorialViewModel", errorMsg)
                    _errorMessage.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error al eliminar peso: ${e.message}"
                Log.e("HistorialViewModel", errorMsg, e)
                _errorMessage.value = errorMsg
            }
        }
    }

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