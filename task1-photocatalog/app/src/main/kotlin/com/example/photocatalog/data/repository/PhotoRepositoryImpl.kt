package com.example.photocatalog.data.repository

import com.example.photocatalog.data.remote.RetrofitInstance
import com.example.photocatalog.data.remote.dto.toDomain
import com.example.photocatalog.domain.model.Photo
import com.example.photocatalog.domain.repository.PhotoRepository

class PhotoRepositoryImpl : PhotoRepository {
    override suspend fun getPhotos(page: Int, limit: Int): List<Photo> =
        RetrofitInstance.api.getPhotos(page = page, limit = limit).map { it.toDomain() }
}
