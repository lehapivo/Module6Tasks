package com.example.nobelprizes.data.repository

import com.example.nobelprizes.data.remote.KtorClient
import com.example.nobelprizes.data.remote.dto.NobelPrizesResponse
import com.example.nobelprizes.data.remote.dto.toDomain
import com.example.nobelprizes.domain.model.NobelPrize
import com.example.nobelprizes.domain.repository.NobelRepository
import io.ktor.client.call.*
import io.ktor.client.request.*

class NobelRepositoryImpl : NobelRepository {
    private val baseUrl = "https://api.nobelprize.org/2.1"

    override suspend fun getPrizes(year: String?, category: String?, limit: Int, offset: Int): List<NobelPrize> {
        val response: NobelPrizesResponse = KtorClient.client.get("$baseUrl/nobelPrizes") {
            parameter("limit", limit)
            parameter("offset", offset)
            if (!year.isNullOrBlank()) parameter("nobelPrizeYear", year)
            if (!category.isNullOrBlank()) parameter("nobelPrizeCategory", category)
        }.body()
        return response.nobelPrizes.map { it.toDomain() }
    }
}
