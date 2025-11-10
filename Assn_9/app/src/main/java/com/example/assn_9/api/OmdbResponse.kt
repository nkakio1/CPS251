package com.example.assn_9.api

import com.google.gson.annotations.SerializedName

// ---------- Search response ----------

data class OmdbSearchResponse(
    @SerializedName("Search") val search: List<MovieSearchItem>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val response: String?,   // "True"/"False"
    @SerializedName("Error") val error: String?
)

data class MovieSearchItem(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("imdbID") val imdbID: String?,
    @SerializedName("Type") val type: String?,
    @SerializedName("Poster") val poster: String?
)

// ---------- Details response ----------

data class OmdbMovieDetailResponse(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("Rated") val rated: String?,
    @SerializedName("Director") val director: String?,
    @SerializedName("Actors") val actors: String?,
    @SerializedName("Plot") val plot: String?,
    @SerializedName("Poster") val poster: String?,
    @SerializedName("BoxOffice") val boxOffice: String?,
    @SerializedName("imdbRating") val imdbRating: String?,
    @SerializedName("Ratings") val ratings: List<Rating>?,
    @SerializedName("Response") val response: String?,
    @SerializedName("Error") val error: String?
) {
    val rottenTomatoes: String?
        get() = ratings?.firstOrNull { it.source == "Rotten Tomatoes" }?.value
}

data class Rating(
    @SerializedName("Source") val source: String?,
    @SerializedName("Value") val value: String?
)
