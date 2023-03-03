package com.example.mynotes.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.AddNotesResponse
import com.example.mynotes.data.remote.response.DetailNotesResponse
import com.example.mynotes.data.repository.notes.NotesRepository
import com.example.mynotes.utils.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailNoteViewModel @Inject constructor(private val repo: NotesRepository, pref: Preference) : ViewModel(){

    private val _detailNotes = MutableLiveData<Resource<DetailNotesResponse>>()
    val detailNotes: LiveData<Resource<DetailNotesResponse>> = _detailNotes

    val getToken = pref.getToken()

    fun getDetailNotes(token: String, diaryId: String) {
        repo.getDetailNotes(token, diaryId).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _detailNotes.value = Resource.Loading
                }
                is Resource.Success -> {
                    result.data.let {
                        _detailNotes.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _detailNotes.value = Resource.Error(result.message, result.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }
}