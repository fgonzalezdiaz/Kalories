package com.example.myapplication.service

import com.example.myapplication.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("api/users/create")
    suspend fun create(@Body user: User): Response<String>

    @POST("api/users/login")
    suspend fun login(
        @Query("email") email: String,
        @Query("contrasena") contrasena: String
    ): Response<User>
}
