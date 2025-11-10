package com.example.assn_9

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.assn_9.api.MovieSearchItem

@Composable
fun MovieSearchScreen(
    navController: NavController,
    movieViewModel: MovieViewModel
) {
    val searchQuery by movieViewModel.searchQuery.collectAsState()
    val searchResults by movieViewModel.searchResults.collectAsState()
    val isLoading by movieViewModel.isLoading.collectAsState()
    val error by movieViewModel.error.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = movieViewModel::onSearchQueryChange,
                label = { Text("Movie Title") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(onClick = { movieViewModel.searchMovies() }) { Text("Search") }
        }
        item {
            if (isLoading) CircularProgressIndicator()
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
        items(searchResults) { movie ->
            MovieCard(movie) { id -> navController.navigate("movie_details/$id") }
        }
    }
}

@Composable
private fun MovieCard(item: MovieSearchItem, onClick: (String) -> Unit) {
    Card(
        onClick = { item.imdbID?.let(onClick) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.poster,
                contentDescription = "Poster",
                modifier = Modifier
                    .height(100.dp)
                    .width(70.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(item.title ?: "No Title", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(item.year ?: "", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
