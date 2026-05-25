package com.example.photocatalog.domain.usecase

import com.example.photocatalog.domain.model.Photo
import com.example.photocatalog.domain.repository.PhotoRepository

class GetPhotosUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(page: Int = 1): List<Photo> =
        repository.getPhotos(page = page)
}
