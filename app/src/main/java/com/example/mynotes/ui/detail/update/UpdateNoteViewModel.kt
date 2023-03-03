package com.example.mynotes.ui.detail.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.UpdateNoteResponse
import com.example.mynotes.data.repository.notes.NotesRepository
import com.example.mynotes.utils.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UpdateNoteViewModel @Inject constructor(private val repo: NotesRepository, pref: Preference) : ViewModel()  {

    private val _updateNotes = MutableLiveData<Resource<UpdateNoteResponse>>()
    val updateNotes: LiveData<Resource<UpdateNoteResponse>> = _updateNotes

    val getToken = pref.getToken()

    fun updateNotes(token: String, diaryId: String, title: String, content: String) {
        repo.updateNote(token, diaryId, title, content).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _updateNotes.value = Resource.Loading
                }
                is Resource.Success -> {
                    result.data.let {
                        _updateNotes.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _updateNotes.value = Resource.Error(result.message, result.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

}