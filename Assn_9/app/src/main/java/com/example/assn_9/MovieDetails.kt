package com.example.assn_9

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.assn_9.api.OmdbMovieDetailResponse
import androidx.compose.ui.graphics.painter.ColorPainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    navController: NavController,
    imdbId: String,
    movieViewModel: MovieViewModel
) {
    var details by remember { mutableStateOf<OmdbMovieDetailResponse?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(imdbId) {
        loading = true
        details = movieViewModel.getMovieDetailsById(imdbId)
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(details?.title ?: "Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (loading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else details?.let { m ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val posterData = m.poster?.takeIf { !it.isNullOrBlank() && it != "N/A" }

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(posterData)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Poster",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit,
                            // ✅ Use solid-color painters instead of system drawables that crash
                            placeholder = ColorPainter(Color(0xFFEAEAEA)),
                            error = ColorPainter(Color(0xFFEEEEEE)),
                            fallback = ColorPainter(Color(0xFFEEEEEE))
                        )

                        Text(
                            m.title ?: "No Title",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Info("Year", m.year)
                        Info("Rated", m.rated)
                        Info("Director", m.director)
                        Info("Actors", m.actors)
                        Info("Rotten Tomatoes", m.rottenTomatoes ?: "N/A")
                        Info("IMDb Rating", m.imdbRating ?: "N/A")
                        Info("Box Office", m.boxOffice ?: "N/A")
                        Text("Plot:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(m.plot ?: "N/A")
                    }
                }
            }
        } ?: Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Movie not found!", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun Info(label: String, value: String?) {
    Text("$label: ${value ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
}
