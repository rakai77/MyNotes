package com.example.mynotes.data.remote.api

import com.example.mynotes.data.remote.response.DetailNotesResponse
import com.example.mynotes.data.remote.response.ListNotesResponse
import com.example.mynotes.data.remote.response.LoginResponse
import com.example.mynotes.data.remote.response.RegisterResponse
import retrofit2.http.*

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

    @GET("/diary")
    @Headers("Accept: application/json")
    suspend fun getListNotes(
        @Header("Authorization") token: String,
    ): ListNotesResponse

    @GET("/diary/{diary_id}")
    @Headers("Accept: application/json")
    suspend fun getDetailNotes(
        @Header("Authorization") token: String,
        @Path("diary_id") diaryId: String?
    ): DetailNotesResponse

}