package com.example.photocatalog.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photocatalog.data.repository.PhotoRepositoryImpl
import com.example.photocatalog.domain.model.Photo
import com.example.photocatalog.domain.usecase.GetPhotosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PhotoListState {
    object Loading : PhotoListState()
    data class Success(val photos: List<Photo>) : PhotoListState()
    data class Error(val message: String) : PhotoListState()
}

class PhotoListViewModel : ViewModel() {
    private val useCase = GetPhotosUseCase(PhotoRepositoryImpl())

    private val _state = MutableStateFlow<PhotoListState>(PhotoListState.Loading)
    val state: StateFlow<PhotoListState> = _state

    init { loadPhotos() }

    fun loadPhotos() {
        viewModelScope.launch {
            _state.value = PhotoListState.Loading
            try {
                val photos = useCase()
                _state.value = PhotoListState.Success(photos)
            } catch (e: Exception) {
                _state.value = PhotoListState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}
