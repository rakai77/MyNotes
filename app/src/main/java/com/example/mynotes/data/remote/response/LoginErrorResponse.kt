package com.example.mynotes.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginErrorResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: List<Any>
)
