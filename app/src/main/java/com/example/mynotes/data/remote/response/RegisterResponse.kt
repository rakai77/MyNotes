package com.example.mynotes.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("id")
	val id: String?,

	@field:SerializedName("email")
	val email: String?,

	@field:SerializedName("username")
	val username: String?
)
