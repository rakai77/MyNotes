package com.example.mynotes.data.repository.auth

import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.api.ApiService
import com.example.mynotes.data.remote.response.LoginResponse
import com.example.mynotes.data.remote.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject


class AuthRepositoryImp @Inject constructor(private val apiService: ApiService) : AuthRepository {

    override fun login(email: String, password: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Resource.Success(result))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage, null))
        }
    }

    override fun register(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.register(email, username, password, confirmPassword)
            emit(Resource.Success(result))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage, null))
        }
    }
}