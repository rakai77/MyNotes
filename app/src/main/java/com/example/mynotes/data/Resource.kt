package com.example.mynotes.data

import okhttp3.RequestBody
import okhttp3.ResponseBody

sealed class Resource<out R> private constructor() {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String?, val errorBody: ResponseBody?) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}