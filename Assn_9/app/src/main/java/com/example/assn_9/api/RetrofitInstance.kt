package com.example.assn_9.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Matches the site’s Chapter 12 pattern:
 * - Singleton object that builds Retrofit
 * - Base URL ends with "/"
 * - Gson converter
 */
object RetrofitInstance {
    const val API_KEY = "9cee3257"
    private const val BASE_URL = "https://www.omdbapi.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)                                  // like the site’s example
        .addConverterFactory(GsonConverterFactory.create()) // JSON <-> Kotlin
        .client(client)
        .build()

    val api: OmdbApiService by lazy { retrofit.create(OmdbApiService::class.java) }
}
