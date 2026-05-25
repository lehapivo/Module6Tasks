package com.example.nobelprizes.domain.repository

import com.example.nobelprizes.domain.model.NobelPrize

interface NobelRepository {
    suspend fun getPrizes(year: String? = null, category: String? = null, limit: Int = 25, offset: Int = 0): List<NobelPrize>
}
