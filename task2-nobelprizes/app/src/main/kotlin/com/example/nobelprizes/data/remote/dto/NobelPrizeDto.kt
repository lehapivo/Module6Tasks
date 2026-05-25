package com.example.nobelprizes.data.remote.dto

import com.example.nobelprizes.domain.model.Laureate
import com.example.nobelprizes.domain.model.NobelPrize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizesResponse(
    @SerialName("nobelPrizes") val nobelPrizes: List<NobelPrizeDto> = emptyList()
)

@Serializable
data class NobelPrizeDto(
    @SerialName("awardYear") val awardYear: String = "",
    @SerialName("category") val category: CategoryDto? = null,
    @SerialName("laureates") val laureates: List<LaureateDto> = emptyList()
)

@Serializable
data class CategoryDto(
    @SerialName("en") val en: String = ""
)

@Serializable
data class LaureateDto(
    @SerialName("id") val id: String? = null,
    @SerialName("fullName") val fullName: NameDto? = null,
    @SerialName("portion") val portion: String? = null,
    @SerialName("motivation") val motivation: MotivationDto? = null,
    @SerialName("birth") val birth: BirthDto? = null,
    @SerialName("links") val links: List<LinkDto> = emptyList()
)

@Serializable
data class NameDto(
    @SerialName("en") val en: String? = null
)

@Serializable
data class MotivationDto(
    @SerialName("en") val en: String? = null
)

@Serializable
data class BirthDto(
    @SerialName("place") val place: PlaceDto? = null
)

@Serializable
data class PlaceDto(
    @SerialName("country") val country: NameDto? = null
)

@Serializable
data class LinkDto(
    @SerialName("rel") val rel: String? = null,
    @SerialName("href") val href: String? = null
)

fun NobelPrizeDto.toDomain() = NobelPrize(
    awardYear = awardYear,
    category = category?.en ?: "Unknown",
    laureates = laureates.map { it.toDomain() }
)

fun LaureateDto.toDomain() = Laureate(
    id = id,
    fullName = fullName?.en ?: "Организация",
    portion = portion,
    motivation = motivation?.en,
    birthCountry = birth?.place?.country?.en,
    portraitUrl = links.firstOrNull { it.rel == "portrait" }?.href
)
