package com.example.assn_9.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API interface following the Chapter 12 pattern:
 * - Interface with @GET and @Query methods
 * - Retrofit creates the implementation at runtime
 */
interface OmdbApiService {

    // Example full URL: https://www.omdbapi.com/?s=batman&apikey=9cee3257
    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = RetrofitInstance.API_KEY
    ): OmdbSearchResponse

    // Example full URL: https://www.omdbapi.com/?i=tt0372784&apikey=9cee3257
    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = RetrofitInstance.API_KEY
    ): OmdbMovieDetailResponse
}
