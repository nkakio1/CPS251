package com.example.assn_9

import com.example.assn_9.api.OmdbApiService
import com.example.assn_9.api.OmdbMovieDetailResponse
import com.example.assn_9.api.OmdbSearchResponse

/**
 * Repository = single place that talks to Retrofit
 * (Chapter 12 uses a similar separation of concerns.)
 */
class MovieRepository(private val api: OmdbApiService) {

    suspend fun searchMovies(query: String): Result<OmdbSearchResponse> = runCatching {
        api.searchMovies(query)
    }

    suspend fun getMovieDetails(imdbId: String): Result<OmdbMovieDetailResponse> = runCatching {
        api.getMovieDetails(imdbId)
    }
}
