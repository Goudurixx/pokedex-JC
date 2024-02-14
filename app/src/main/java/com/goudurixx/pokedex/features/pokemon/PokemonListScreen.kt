package com.goudurixx.pokedex.features.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PokemonListRoute(
    navigateToPokemonDetail: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PokemonListScreen(uiState = uiState, navigateToPokemonDetail = navigateToPokemonDetail)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    uiState: PokemonListUiState,
    navigateToPokemonDetail: (Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Blue
                    )
                )
            )
    ) {
        Column {
            Icon(
                Icons.Filled.BrokenImage,
                null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(16.dp)
            )
            DockedSearchBar(
                query = "SearchPokemonHere",
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {}
            when (uiState) {
                is PokemonListUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is PokemonListUiState.Error -> {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }

                is PokemonListUiState.Success -> {
                    LazyColumn {
                        items(uiState.data.results) { pokemon ->
                            val image = ImageRequest.Builder(LocalContext.current)
                                .data(pokemon.image)
                                .build()
                            OutlinedButton(
                                onClick = { navigateToPokemonDetail(pokemon.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                AsyncImage(model = image, contentDescription = "Pokemon Image")
                                Text(text = pokemon.name + " " + pokemon.id)
                            }
                            Divider(modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}