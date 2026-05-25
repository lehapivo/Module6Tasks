package com.example.nobelserver.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NobelPrize(
    val id: Int = 0,
    val awardYear: String,
    val category: String,
    val fullName: String,
    val motivation: String,
    val detailLink: String = ""
)

@Serializable
data class Laureate(
    val id: Int = 0,
    val prizeId: Int,
    val fullName: String,
    val portion: String,
    val motivation: String,
    val portraitUrl: String? = null
)

data class User(
    val id: Int = 0,
    val username: String,
    val passwordHash: String,
    val role: String = "user"
)
