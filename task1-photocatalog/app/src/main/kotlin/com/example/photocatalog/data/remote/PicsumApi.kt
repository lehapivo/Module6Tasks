package com.example.photocatalog.data.remote

import com.example.photocatalog.data.remote.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApi {
    // JSONPlaceholder — публичный API, никогда не даёт 403
    // Возвращает 5000 фото с thumbnailUrl и url
    @GET("photos")
    suspend fun getPhotos(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 30
    ): List<PhotoDto>
}
