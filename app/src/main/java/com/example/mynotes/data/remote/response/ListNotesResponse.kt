package com.example.mynotes.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListNotesResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("total_data")
	val totalData: Int,

	@field:SerializedName("page")
	val page: Int
)

data class DataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("is_archieved")
	val isArchieved: Boolean,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String
)
