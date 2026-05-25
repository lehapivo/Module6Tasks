package com.example.nobelclient.data.remote.dto

import com.example.nobelclient.domain.model.NobelPrize
import kotlinx.serialization.Serializable

/**
 * DTO matching the response from our own Ktor server (Task 5).
 * Server returns: List<NobelPrizeDto> directly (no wrapper object).
 */
@Serializable
data class NobelPrizeDto(
    val id: Int = 0,
    val awardYear: String = "",
    val category: String = "",
    val fullName: String = "",
    val motivation: String = "",
    val detailLink: String = ""
)

fun NobelPrizeDto.toDomain() = NobelPrize(
    awardYear = awardYear,
    category = category,
    laureates = listOf(
        com.example.nobelclient.domain.model.Laureate(
            id = id.toString(),
            fullName = fullName,
            portion = "1/1",
            motivation = motivation,
            birthCountry = null,
            portraitUrl = null
        )
    )
)
