package com.example.mynotes.data.repository.notes

import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.AddNotesResponse
import com.example.mynotes.data.remote.response.DetailNotesResponse
import com.example.mynotes.data.remote.response.ListNotesResponse
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun getAllNotes(token: String) : Flow<Resource<ListNotesResponse>>

    fun getDetailNotes(token: String, diaryId: String) : Flow<Resource<DetailNotesResponse>>

    fun addNotes(token: String, title: String, content: String) : Flow<Resource<AddNotesResponse>>
}