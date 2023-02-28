package com.example.mynotes.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.LoginResponse
import com.example.mynotes.data.remote.response.RegisterResponse
import com.example.mynotes.data.repository.auth.AuthRepository
import com.example.mynotes.utils.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepository, private val pref: Preference) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<LoginResponse>>()
    val loginState: LiveData<Resource<LoginResponse>> = _loginState


    private val _registerState = MutableLiveData<Resource<RegisterResponse>>()
    val registerState: LiveData<Resource<RegisterResponse>> = _registerState

    val getToken = pref.getToken()

    fun login(email: String, password: String) {
        repo.login(email, password).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _loginState.value = Resource.Loading
                }
                is Resource.Success -> {
                    result.data.let {
                        _loginState.value = Resource.Success(it)
                        pref.saveToken(it.accessToken.toString())
                        pref.saveUsername(it.user?.username.toString())
                        pref.saveEmailUser(it.user?.email.toString())
                    }
                }
                is Resource.Error -> {
                    _loginState.value = Resource.Error(result.message,  result.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun register(username: String, email: String, password: String, confirmPassword: String) {
        repo.register(username, email, password, confirmPassword).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _registerState.value = Resource.Loading
                }
                is Resource.Success -> {
                    result.data.let {
                        _registerState.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _registerState.value = Resource.Error(result.message,  result.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

}