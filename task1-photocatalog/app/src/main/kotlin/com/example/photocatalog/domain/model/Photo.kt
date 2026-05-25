package com.example.photocatalog.domain.model

data class Photo(
    val id: String,
    val picsumId: Int,
    val author: String,
    val width: Int,
    val height: Int,
    val title: String,
    val thumbnailUrl: String,
    val fullImageUrl: String
)
