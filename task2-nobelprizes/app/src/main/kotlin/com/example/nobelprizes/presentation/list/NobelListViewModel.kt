package com.example.nobelprizes.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nobelprizes.data.repository.NobelRepositoryImpl
import com.example.nobelprizes.domain.model.NobelPrize
import com.example.nobelprizes.domain.usecase.GetPrizesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PrizeListState {
    object Loading : PrizeListState()
    data class Success(val prizes: List<NobelPrize>) : PrizeListState()
    data class Error(val message: String) : PrizeListState()
}

val CATEGORIES = listOf("", "physics", "chemistry", "literature", "peace", "medicine", "economics")

class NobelListViewModel : ViewModel() {
    private val useCase = GetPrizesUseCase(NobelRepositoryImpl())

    private val _state = MutableStateFlow<PrizeListState>(PrizeListState.Loading)
    val state: StateFlow<PrizeListState> = _state

    val selectedYear = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("")

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = PrizeListState.Loading
            try {
                val prizes = useCase(
                    year = selectedYear.value.takeIf { it.isNotBlank() },
                    category = selectedCategory.value.takeIf { it.isNotBlank() }
                )
                _state.value = PrizeListState.Success(prizes)
            } catch (e: Exception) {
                _state.value = PrizeListState.Error(e.message ?: "Ошибка")
            }
        }
    }
}
