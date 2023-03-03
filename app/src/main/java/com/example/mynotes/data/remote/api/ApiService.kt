package com.example.mynotes.data.remote.api

import com.example.mynotes.data.remote.response.*
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

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("/diary")
    suspend fun addNotes(
        @Header("Authorization") token: String,
        @Field("title") title: String,
        @Field("content") content: String,
    ): AddNotesResponse

    @FormUrlEncoded
    @PUT("/diary/{diary_id}")
    @Headers("Accept: application/json")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Path("diary_id") diaryId: String?,
        @Field("title") title: String,
        @Field("content") content: String,
    ): UpdateNoteResponse

}