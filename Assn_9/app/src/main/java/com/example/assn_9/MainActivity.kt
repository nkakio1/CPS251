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
}
