package com.example.nobelclient.domain.repository

import com.example.nobelclient.domain.model.NobelPrize

interface NobelRepository {
    suspend fun getPrizes(year: String? = null, category: String? = null, limit: Int = 25, offset: Int = 0): List<NobelPrize>
}
