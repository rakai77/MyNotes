package com.example.mynotes.ui.home.addnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.AddNotesResponse
import com.example.mynotes.data.repository.notes.NotesRepository
import com.example.mynotes.utils.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(private val repo: NotesRepository, pref: Preference) : ViewModel(){

    private val _addNotes = MutableLiveData<Resource<AddNotesResponse>>()
    val addNotes: LiveData<Resource<AddNotesResponse>> = _addNotes

    val getToken = pref.getToken()

    fun addNotes(token: String, title: String, content: String) {
        repo.addNotes(token, title, content).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _addNotes.value = Resource.Loading
                }
                is Resource.Success -> {
                    result.data.let {
                        _addNotes.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _addNotes.value = Resource.Error(result.message, result.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }
}