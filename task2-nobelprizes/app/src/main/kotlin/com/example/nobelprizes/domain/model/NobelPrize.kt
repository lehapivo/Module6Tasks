package com.example.nobelprizes.domain.model

data class NobelPrize(
    val awardYear: String,
    val category: String,
    val laureates: List<Laureate>
)

data class Laureate(
    val id: String?,
    val fullName: String,
    val portion: String?,
    val motivation: String?,
    val birthCountry: String?,
    val portraitUrl: String?
)
