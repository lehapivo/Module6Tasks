package com.example.photocatalog.data.remote.dto

import com.example.photocatalog.domain.model.Photo
import com.google.gson.annotations.SerializedName

data class PhotoDto(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)

// Known picsum photo authors and sizes (picsum IDs 1-30)
private val picsumAuthors = mapOf(
    1 to Triple("Alejandro Escamilla", 5616, 3744),
    2 to Triple("Alejandro Escamilla", 5616, 3744),
    3 to Triple("Alejandro Escamilla", 5616, 3744),
    10 to Triple("Paul Jarvis", 2500, 1667),
    11 to Triple("Paul Jarvis", 2500, 1667),
    12 to Triple("Paul Jarvis", 2500, 1667),
    13 to Triple("Oliver Schwendener", 5000, 3333),
    14 to Triple("Thomas Lefebvre", 6000, 3376),
    15 to Triple("Thomas Lefebvre", 6000, 3376),
    16 to Triple("Thomas Lefebvre", 6000, 3376),
    20 to Triple("Olivier Miche", 3008, 2000),
    21 to Triple("Danielle MacInnes", 4928, 3264),
    22 to Triple("Danielle MacInnes", 4928, 3264),
    25 to Triple("Vadim Sherbakov", 5120, 2880),
    26 to Triple("Vadim Sherbakov", 5120, 2880),
    27 to Triple("Vadim Sherbakov", 5120, 2880),
    28 to Triple("Vadim Sherbakov", 5120, 2880),
    29 to Triple("Vadim Sherbakov", 5120, 2880),
    30 to Triple("Vadim Sherbakov", 5120, 2880),
)

fun PhotoDto.toDomain(): Photo {
    // Map JSONPlaceholder id (1-5000) to picsum id (1-1084)
    val picsumId = (id % 1000).coerceAtLeast(1)
    val meta = picsumAuthors[picsumId] ?: Triple("Photographer #$picsumId", 1920, 1080)

    return Photo(
        id = id.toString(),
        picsumId = picsumId,
        author = meta.first,
        width = meta.second,
        height = meta.third,
        title = title,
        // Direct picsum CDN URL — works on Android, no redirect issues
        thumbnailUrl = "https://picsum.photos/id/$picsumId/400/300",
        fullImageUrl  = "https://picsum.photos/id/$picsumId/${meta.second.coerceAtMost(1200)}/${meta.third.coerceAtMost(900)}"
    )
}
