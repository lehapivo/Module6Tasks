package com.example.nobelprizes.domain.usecase

import com.example.nobelprizes.domain.model.NobelPrize
import com.example.nobelprizes.domain.repository.NobelRepository

class GetPrizesUseCase(private val repository: NobelRepository) {
    suspend operator fun invoke(year: String? = null, category: String? = null): List<NobelPrize> =
        repository.getPrizes(year = year, category = category)
}
