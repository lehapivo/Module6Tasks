package com.example.photocatalog.domain.repository

import com.example.photocatalog.domain.model.Photo

interface PhotoRepository {
    suspend fun getPhotos(page: Int = 1, limit: Int = 30): List<Photo>
}
