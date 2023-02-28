package com.example.mynotes.data.repository.auth

import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.LoginResponse
import com.example.mynotes.data.remote.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun login(email: String, password: String) : Flow<Resource<LoginResponse>>

    fun register(username: String, email: String, password: String, confirmPassword: String) : Flow<Resource<RegisterResponse>>

}