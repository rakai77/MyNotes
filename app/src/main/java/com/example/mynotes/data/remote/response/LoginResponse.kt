package com.example.mynotes.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("access_token")
	val accessToken: String?,

	@field:SerializedName("user")
	val user: User?
)

data class User(

	@field:SerializedName("id")
	val id: String?,

	@field:SerializedName("email")
	val email: String?,

	@field:SerializedName("username")
	val username: String?
)

data class IncorrectEmail(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: List<Any>
)
