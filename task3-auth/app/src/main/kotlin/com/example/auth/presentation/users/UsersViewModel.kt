package com.example.auth.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.data.repository.UserRepositoryImpl
import com.example.auth.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<User>) : UsersState()
    data class Error(val message: String) : UsersState()
}

class UsersViewModel : ViewModel() {
    private val repo = UserRepositoryImpl()
    private val _state = MutableStateFlow<UsersState>(UsersState.Loading)
    val state: StateFlow<UsersState> = _state

    fun load(token: String) {
        viewModelScope.launch {
            _state.value = UsersState.Loading
            try {
                val users = repo.getUsers(token)
                _state.value = UsersState.Success(users)
            } catch (e: Exception) {
                _state.value = UsersState.Error(e.message ?: "Ошибка")
            }
        }
    }
}
