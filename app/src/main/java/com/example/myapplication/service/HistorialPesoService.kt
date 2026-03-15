package com.example.myapplication.service

import com.example.myapplication.model.HistorialPeso
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface HistorialPesoService {
    @GET("api/historial-peso/findAll")
    suspend fun findAll(@Query("id_user") idUser: Long) : Response<List<HistorialPeso>>

    @GET("api/historial-peso/findByIdUser")
    suspend fun findByIdUser(@Query("id_user") idUser: Long) : Response<List<HistorialPeso>>

    @POST("api/historial-peso/create")
    suspend fun create(
        @Query("fecha") fecha: String,
        @Query("peso") peso: Int,
        @Query("id_user") idUser: Long
    ): Response<Void>

    @PUT("api/historial-peso/update")
    suspend fun update(
        @Query("id") id: Long,
        @Query("fecha") fecha: String,
        @Query("peso") peso: Int,
        @Query("id_user") idUser: Long
    ): Response<Void>

    @GET("api/historial-peso/findByUserAndWeight")
    suspend fun findByUserAndWeight(
            @Query("id_user")id_user : Long,
            @Query("peso") peso : Int
    ): Response<List<HistorialPeso>>

    @HTTP(method = "DELETE", path = "api/historial-peso/delete", hasBody = true)
    suspend fun delete(@Body historialPeso : HistorialPeso): Response<Void>

    /* Puede que haya un problema con el findByPeso ya que tendria que buscar
     por id y peso
    @GET("api/historial-peso/findByPeso/")
    suspend fun findByPeso( @Query("peso") peso : Int): Response<List<HistorialPeso>>

     Puede que haya un problema con el findByPeso ya que tendria que buscar
     por id y peso
    @GET("api/historial-peso/findByFecha")
    suspend fun findByFecha( @Query("fecha") fecha : String ): Response<List<HistorialPeso>>
    */
}