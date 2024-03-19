package com.goudurixx.pokedex.features.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.goudurixx.pokedex.features.pokemon.components.PokemonList
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import java.util.Locale

@Composable
fun PokemonTypeRoute(
    typeName: String,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick : () -> Unit,
    viewModel: PokemonTypeViewModel = hiltViewModel(),
) {

    val pokemonLazyPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PokemonTypeScreen(
        typeName = typeName,
        uiState = uiState,
        pokemonLazyPagingItems = pokemonLazyPagingItems,
        navigateToPokemonDetail = navigateToPokemonDetail,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonTypeScreen(
    typeName: String,
    uiState: TypeUiState,
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick : () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = { Text(text = typeName.capitalize(Locale.ROOT)) }, navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, null)
            }
        })
    }) {
        when (uiState) {
            is TypeUiState.Success -> Box(modifier = Modifier.background(uiState.data.color)) {
                PokemonList(
                    pokemonLazyPagingItems = pokemonLazyPagingItems,
                    onItemClick = navigateToPokemonDetail,
                    modifier = Modifier.padding(it),
                    contentPadding = PaddingValues(16.dp)
                )
                Text(text = uiState.data.name)
            }


            is TypeUiState.Loading -> CircularProgressIndicator()
            is TypeUiState.Error -> Text("Error loading data")
        }
    }
}
