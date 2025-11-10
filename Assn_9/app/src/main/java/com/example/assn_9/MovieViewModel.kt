package com.example.assn_9

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assn_9.api.MovieSearchItem
import com.example.assn_9.api.OmdbMovieDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel keeps network calls off the UI thread (see Chapter 12 & 10 discussion).
 */
class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MovieSearchItem>>(emptyList())
    val searchResults: StateFlow<List<MovieSearchItem>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }

    fun searchMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _searchResults.value = emptyList()

            val q = _searchQuery.value
            if (q.isBlank()) {
                _error.value = "Search query cannot be empty"
                _isLoading.value = false
                return@launch
            }

            repository.searchMovies(q)
                .onSuccess { resp ->
                    if (resp.response == "True" && resp.search != null) {
                        _searchResults.value = resp.search
                    } else {
                        _error.value = resp.error ?: "Movie not found!"
                    }
                }
                .onFailure { _error.value = it.message ?: "Unknown error" }

            _isLoading.value = false
        }
    }

    suspend fun getMovieDetailsById(imdbId: String): OmdbMovieDetailResponse? {
        _isLoading.value = true
        _error.value = null
        val out = repository.getMovieDetails(imdbId).getOrElse {
            _error.value = it.message ?: "Error fetching details"
            null
        }
        _isLoading.value = false
        return out?.takeIf { it.response == "True" }
    }

    companion object {
        fun provideFactory(repository: MovieRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MovieViewModel(repository) as T
                }
            }
    }
}
