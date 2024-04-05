package com.goudurixx.pokedex.features.pokemon.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.goudurixx.pokedex.core.ui.component.PokemonListItem
import com.goudurixx.pokedex.core.ui.component.PokemonListItemLoading
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem
import kotlinx.coroutines.launch

@Composable
internal fun BoxScope.PokemonList(
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    onItemClick: (Int, Int) -> Unit,
    onUpdateFavorite: (Int, Boolean) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    selectedFilter: SortOrderItem? = null,
    enabled: Boolean = true,
) {
    val state = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    val showScrollToTopButton by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 5
        }
    }

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val itemWidth = 300.dp
    val itemHeight = 100.dp
    val gridWidth = LocalConfiguration.current.screenWidthDp.dp
    val gridHeight = LocalConfiguration.current.screenHeightDp.dp
    val numberOfItemsPerRow = (gridWidth / itemWidth).toInt()
    val numberOfRows = (gridHeight / itemHeight).toInt()
    val numberOfItems = pokemonLazyPagingItems.itemCount
    val remainder = numberOfItems % numberOfItemsPerRow
    val placeholdersNeeded = if (remainder != 0) numberOfItemsPerRow - remainder else 0

    LazyVerticalGrid(
        columns = GridCells.Adaptive(itemWidth),
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size = it.size
            },
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = pokemonLazyPagingItems.itemCount,
            key = pokemonLazyPagingItems.itemKey { pokemon -> pokemon.index },
            contentType = pokemonLazyPagingItems.itemContentType { "Pokemon" }
        ) { index: Int ->

            val pokemon: PokemonListItemUiModel? = pokemonLazyPagingItems[index]
            if (pokemon != null) {
                PokemonListItem(
                    pokemon = pokemon,
                    enabled = enabled,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer.toArgb(),
                    selectedFilter = selectedFilter,
                    onItemClick = onItemClick,
                    onUpdateFavorite = onUpdateFavorite,
                    itemHeight = 100.dp,
                    modifier = Modifier,
                )
            }
        }
        items(count = placeholdersNeeded + numberOfItemsPerRow * numberOfRows) {
            if (pokemonLazyPagingItems.loadState.refresh is LoadState.Loading || pokemonLazyPagingItems.loadState.append is LoadState.Loading)
                PokemonListItemLoading(itemHeight = 100.dp)
        }
        items(count = placeholdersNeeded) {
            if (pokemonLazyPagingItems.loadState.append.endOfPaginationReached || pokemonLazyPagingItems.loadState.refresh is LoadState.Error)
                Spacer(modifier = Modifier.fillMaxWidth())
        }
        item {
            if (pokemonLazyPagingItems.loadState.append.endOfPaginationReached) {
                Text(
                    text = if (pokemonLazyPagingItems.itemCount == 0) "No pokemons matching the selected filters" else "No more pokemons to load",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                )
            }
            if (pokemonLazyPagingItems.loadState.refresh is LoadState.Error) {
                OutlinedButton(
                    onClick = { pokemonLazyPagingItems.retry() },
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    Text(text = "Retry")
                }
            }
        }
    }


    AnimatedVisibility(
        visible = showScrollToTopButton,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it },
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(contentPadding)
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    state.animateScrollToItem(0)
                }
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Scroll to top"
            )
        }
    }
}