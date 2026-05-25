package com.example.nobelserver.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NobelPrize(
    val id: String,
    val awardYear: String,
    val category: String,
    val laureates: List<Laureate>
)

@Serializable
data class Laureate(
    val id: String,
    val fullName: String,
    val portion: String,
    val motivation: String,
    val birthCountry: String? = null
)

data class User(
    val username: String,
    val passwordHash: String,
    val role: String = "user"
)
