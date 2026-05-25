package com.example.nobelclient.domain.usecase

import com.example.nobelclient.domain.model.NobelPrize
import com.example.nobelclient.domain.repository.NobelRepository

class GetPrizesUseCase(private val repository: NobelRepository) {
    suspend operator fun invoke(year: String? = null, category: String? = null): List<NobelPrize> =
        repository.getPrizes(year = year, category = category)
}
