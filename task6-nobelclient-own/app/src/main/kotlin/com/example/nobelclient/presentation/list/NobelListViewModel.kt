package com.example.nobelclient.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nobelclient.data.repository.NobelRepositoryImpl
import com.example.nobelclient.domain.model.NobelPrize
import com.example.nobelclient.domain.usecase.GetPrizesUseCase
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
                // Our own server doesn't filter by year/category yet —
                // it returns all prizes. Filter client-side.
                val allPrizes = useCase()
                val filtered = allPrizes.filter { prize ->
                    val yearMatch = selectedYear.value.isBlank() || prize.awardYear == selectedYear.value
                    val catMatch = selectedCategory.value.isBlank() ||
                        prize.category.lowercase().contains(selectedCategory.value.lowercase())
                    yearMatch && catMatch
                }
                _state.value = PrizeListState.Success(filtered)
            } catch (e: Exception) {
                _state.value = PrizeListState.Error(
                    "Ошибка подключения к серверу: ${e.message}\n" +
                    "Убедитесь, что сервер (Task 5) запущен на ${NobelRepositoryImpl.BASE_URL}"
                )
            }
        }
    }
}
