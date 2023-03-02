package com.example.mynotes.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.ListNotesResponse
import com.example.mynotes.data.repository.notes.NotesRepository
import com.example.mynotes.utils.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: NotesRepository, pref: Preference) : ViewModel()  {

    private val _listNotes = MutableLiveData<Resource<ListNotesResponse>>()
    val listNotes: LiveData<Resource<ListNotesResponse>> = _listNotes

    val getToken = pref.getToken()

    fun getAllNotes(token: String) {
        repo.getAllNotes(token).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _listNotes.value = Resource.Loading
                }
                is Resource.Success -> {
                    result.data.let {
                        _listNotes.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _listNotes.value = Resource.Error(result.message, result.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }
}