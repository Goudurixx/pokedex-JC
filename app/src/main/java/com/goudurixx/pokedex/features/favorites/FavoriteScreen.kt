package com.goudurixx.pokedex.features.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goudurixx.pokedex.core.ui.component.PokemonListItem
import com.goudurixx.pokedex.features.pokemon.PokemonListViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
internal fun FavoriteRoute(
    navigateToPokemonDetail: (Int, Int) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoriteScreen(
        uiState = uiState,
        onUpdateFavorite = viewModel::updateFavorite,
        navigateToPokemonDetail = navigateToPokemonDetail
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteScreen(
    uiState: FavoriteUiState,
    onUpdateFavorite: (Int, Boolean) -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        when (uiState) {
            is FavoriteUiState.Loading -> {
                Text(text = "Loading")
            }

            is FavoriteUiState.Error -> {
                Text(text = "Error")
            }

            is FavoriteUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    stickyHeader {
                        Text(
                            text = "total count : ${uiState.list.size}",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    items(uiState.list, key = { pokemon -> pokemon.id }) { pokemon ->
                        AnimatedVisibility(
                            visible = pokemon.isFavorite, Modifier.animateItemPlacement(
                                animationSpec = spring()
                            ), exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                        ) {
                            PokemonListItem(
                                pokemon = pokemon,
                                enabled = true,
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer.toArgb(),
                                selectedFilter = null,
                                onItemClick = navigateToPokemonDetail,
                                onUpdateFavorite = onUpdateFavorite,
                                itemHeight = 100.dp,
                                modifier = Modifier,
                            )
                        }
                    }
                }
            }
        }
    }
}
