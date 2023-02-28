package com.example.mynotes.data.remote.api

import com.example.mynotes.data.remote.response.LoginResponse
import com.example.mynotes.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse


    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("/auth/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password_confirmation") confirmPassword: String,
    ): RegisterResponse


}