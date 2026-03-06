package com.example.myapplication.service

import com.example.myapplication.model.HistorialPeso
import retrofit2.Response
import retrofit2.http.GET


interface HistorialPesoService {
    @GET("findAll/")
    suspend fun findAll() : Response<List<HistorialPeso>>


}