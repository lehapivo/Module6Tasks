package com.example.nobelclient.data.repository

import com.example.nobelclient.data.remote.KtorClient
import com.example.nobelclient.data.remote.dto.NobelPrizeDto
import com.example.nobelclient.data.remote.dto.toDomain
import com.example.nobelclient.domain.model.NobelPrize
import com.example.nobelclient.domain.repository.NobelRepository
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * Task 6: Connects to OWN Ktor server (Task 5) instead of public Nobel API.
 *
 * Set BASE_URL:
 *  - Local emulator:    http://10.0.2.2:8080
 *  - Physical device:   http://192.168.X.X:8080
 *  - Production:        https://your-server.com
 */
class NobelRepositoryImpl(private val token: String = "") : NobelRepository {
    companion object {
        // ⚠️ Change this to your server address
        const val BASE_URL = "http://192.168.1.3"
    }

    override suspend fun getPrizes(year: String?, category: String?, limit: Int, offset: Int): List<NobelPrize> {
        // Our server returns List<NobelPrize> directly (not wrapped)
        val prizes: List<NobelPrizeDto> = KtorClient.client.get("$BASE_URL/prizes") {
            if (token.isNotBlank()) header("Authorization", "Bearer $token")
        }.body()
        return prizes.map { it.toDomain() }
    }
}
