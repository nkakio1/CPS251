package com.example.assn_9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assn_9.api.RetrofitInstance

class sss : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MovieRepository(RetrofitInstance.api)

        setContent {
            val navController = rememberNavController()
            val movieViewModel: MovieViewModel =
                viewModel(factory = MovieViewModel.provideFactory(repository))

            NavHost(navController = navController, startDestination = "search") {
                composable("search") {
                    MovieSearchScreen(
                        navController = navController,
                        movieViewModel = movieViewModel
                    )
                }
                composable(
                    route = "movie_details/{imdbId}",
                    arguments = listOf(navArgument("imdbId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val imdbId = backStackEntry.arguments?.getString("imdbId") ?: ""
                    MovieDetailsScreen(
                        navController = navController,
                        imdbId = imdbId,
                        movieViewModel = movieViewModel
                    )
                }
            }
        }
    }
    /*
    Questions:
    1
    Explain the role of Retrofit in this project. How does it simplify interacting with the
    OMDB API compared to making raw HTTP requests?
    2
    What is the purpose of the data classes (e.g., OmdbMovieDetailResponse, MovieSearchItem)
    in this project? How do they relate to the JSON responses from the OMDB API?
    3
    Why is a MovieRepository included in this project's architecture? What are the benefits
    of having a separate repository layer for data fetching, especially in a larger application?
    4
    What are the primary responsibilities of the MovieViewModel? How does it help to separate
    concerns between the UI and the data layer, and what advantages does this provide?
    5
    Describe how navigation between the MovieSearchScreen and MovieDetailsScreen is handled
    in this project. How are arguments (like imdbId) passed between composables during navigation?


     */
}
