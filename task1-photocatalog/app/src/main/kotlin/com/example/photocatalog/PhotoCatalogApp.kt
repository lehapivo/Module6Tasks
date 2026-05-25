package com.example.photocatalog

import android.app.Application
import coil.Coil
import coil.ImageLoader
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class PhotoCatalogApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                // Some CDNs require a browser-like User-Agent
                val req = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Android; Mobile)")
                    .build()
                chain.proceed(req)
            }
            .build()

        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .okHttpClient(okHttpClient)
                .crossfade(true)
                .build()
        )
    }
}
