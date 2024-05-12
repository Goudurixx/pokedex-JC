package com.goudurixx.pokedex.features.favorites

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goudurixx.pokedex.core.ui.component.PokemonListItem

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
    ) {
        when (uiState) {
            is FavoriteUiState.Loading -> {
                Text(text = "Loading")
            }

            is FavoriteUiState.Error -> {
                Text(text = "Error")
            }

            is FavoriteUiState.Success -> {

                val gridState = rememberLazyGridState()
                var size by remember {
                    mutableStateOf(IntSize.Zero)
                }

                val itemWidth = 300.dp
                val itemHeight = 100.dp
                AnimatedContent(
                    targetState = uiState.list.isEmpty(),
                    modifier = Modifier.padding(it)
                ) { listState ->
                    AnimatedVisibility(!listState) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(itemWidth),
                            modifier = Modifier
                                .fillMaxSize()
                                .onGloballyPositioned {
                                    size = it.size
                                },
                            state = gridState,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                top = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(
                                count = uiState.list.size,
                                key = { pokemon -> uiState.list[pokemon].id },
                                contentType = { pokemon -> uiState.list[pokemon] }
                            ) {
                                val pokemon = uiState.list[it]
                                AnimatedVisibility(
                                    visible = pokemon.isFavorite,
                                    Modifier.animateItemPlacement(
                                        animationSpec = spring()
                                    ),
                                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                                ) {
                                    PokemonListItem(
                                        pokemon = pokemon,
                                        enabled = true,
                                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer.toArgb(),
                                        selectedFilter = null,
                                        onItemClick = navigateToPokemonDetail,
                                        onUpdateFavorite = onUpdateFavorite,
                                        itemHeight = itemHeight,
                                        modifier = Modifier,
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(visible = listState) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text(text = "No favorite pokemon", modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}
