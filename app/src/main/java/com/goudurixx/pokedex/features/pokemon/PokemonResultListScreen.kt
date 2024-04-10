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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.features.pokemon.components.PokemonList
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import java.util.Locale


@Composable
fun PokemonResultListRoute(
    resultType: FilterByParameter,
    resultName: String,
    resultColor: Int?,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PokemonResultListViewModel = hiltViewModel(),
) {

    val pokemonLazyPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PokemonResultListScreen(
        uiState = uiState,
        resultType = resultType,
        resultName = resultName,
        resultColor = resultColor,
        pokemonLazyPagingItems = pokemonLazyPagingItems,
        onUpdateFavorite = viewModel::updateFavorite,
        navigateToPokemonDetail = navigateToPokemonDetail,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonResultListScreen(
    uiState: ResultListUiState,
    resultType: FilterByParameter,
    resultName: String,
    resultColor: Int?,
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    onUpdateFavorite: (Int, Boolean) -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = resultName.capitalize(Locale.ROOT)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            })
    }) {
        when (uiState) {
            is ResultListUiState.Success -> Box(modifier = Modifier.background(resultColor?.let {
                Color(
                    it
                )
            } ?: MaterialTheme.colorScheme.background)) {
                PokemonList(
                    pokemonLazyPagingItems = pokemonLazyPagingItems,
                    onItemClick = navigateToPokemonDetail,
                    onUpdateFavorite = onUpdateFavorite,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.padding(it),
                )
                Text(text = resultName)
            }


            is ResultListUiState.Loading -> CircularProgressIndicator()
            is ResultListUiState.Error -> Text("Error loading data")
        }
    }
}