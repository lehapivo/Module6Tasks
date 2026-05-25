package com.example.auth.presentation.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.data.repository.TokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AuthRepositoryImpl()
    val tokenStorage = TokenStorage(app)

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val result = repo.login(username, password)
                tokenStorage.saveToken(result.token)
                _state.value = LoginState.Success(result.token)
            } catch (e: Exception) {
                _state.value = LoginState.Error(e.message ?: "Ошибка авторизации")
            }
        }
    }
}
