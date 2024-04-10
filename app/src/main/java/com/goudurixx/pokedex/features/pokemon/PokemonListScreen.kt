package com.goudurixx.pokedex.features.pokemon

import DockedSearchContainer
import FabContainer
import FabContainerState
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.common.models.OrderByValues
import com.goudurixx.pokedex.features.pokemon.components.PokemonList
import com.goudurixx.pokedex.features.pokemon.models.BaseFilterItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem

@Composable
fun PokemonListRoute(
    navigateToPokemonDetail: (Int, Int) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit
) {

    val pokemonLazyPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()

    val search by viewModel.search.collectAsStateWithLifecycle()
    val sortFilterList by viewModel.sortFilterList.collectAsStateWithLifecycle()
    val filterList by viewModel.filterList.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    PokemonListScreen(
        search = search,
        sortFilterList = sortFilterList,
        filterList = filterList,
        searchState = searchState,
        onUpdateSearch = viewModel::updateSearch,
        onUpdateSort = viewModel::updateSort,
        onFilterChange = viewModel::updateFilter,
        onResetFilter = viewModel::resetFilter,
        onUpdateFavorite = viewModel::updateFavorite,
        pokemonLazyPagingItems = pokemonLazyPagingItems,
        navigateToPokemonResultList = navigateToPokemonResultList,
        navigateToPokemonDetail = navigateToPokemonDetail
    )
}

@SuppressLint("RememberReturnType")
@Composable
fun PokemonListScreen(
    search: String,
    sortFilterList: List<SortOrderItem>,
    filterList: List<BaseFilterItemUiModel>,
    searchState: SearchUiState,
    onUpdateSearch: (String) -> Unit,
    onUpdateSort: (SortOrderItem) -> Unit,
    onFilterChange: (List<BaseFilterItemUiModel>) -> Unit,
    onResetFilter: () -> Unit,
    onUpdateFavorite: (Int, Boolean) -> Unit,
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit,
) {

    var filterFabContainerState by remember { mutableStateOf(FabContainerState.Fab) }

    BackHandler(filterFabContainerState == FabContainerState.Fullscreen) {
        filterFabContainerState = FabContainerState.Fab
    }
    val selectedFilter by remember(sortFilterList) {
        derivedStateOf {
            sortFilterList.firstOrNull { it.order != null }
        }
    }
    var searchBarSize by remember { mutableStateOf(IntSize.Zero) }
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {

            DockedSearchContainer(
                sortFilterList = sortFilterList,
                query = search,
                onQueryChange = onUpdateSearch,
                onFilterClick = { sortFilterItem ->
                    when (sortFilterItem.order) {
                        null -> onUpdateSort(sortFilterItem.copy(order = OrderByValues.ASC))
                        OrderByValues.ASC -> onUpdateSort(sortFilterItem.copy(order = OrderByValues.DESC))
                        OrderByValues.DESC -> onUpdateSort(sortFilterItem.copy(order = null))
                    }
                    pokemonLazyPagingItems.refresh()
                },
                onClickOnResult = navigateToPokemonDetail,
                modifier = Modifier
                    .onSizeChanged { newSize ->
                        if (newSize.height < searchBarSize.height || searchBarSize.height == 0) searchBarSize =
                            newSize
                    }
                    .drawBehind {
                        val brush = Brush.verticalGradient(
                            0.0f to backgroundColor,
                            1.0f to backgroundColor.copy(alpha = 0.0f)
                        )
                        drawRect(brush = brush)
                    },
                state = searchState,
                onSearch = {
                    navigateToPokemonResultList(
                        FilterByParameter.NAME,
                        it, //VALUE
                        it, //NAME OF THE SCREEN
                        null
                    )
                },
            )

            PokemonList(
                selectedFilter = selectedFilter,
                pokemonLazyPagingItems = pokemonLazyPagingItems,
                enabled = filterFabContainerState == FabContainerState.Fab,
                onItemClick = navigateToPokemonDetail,
                onUpdateFavorite = onUpdateFavorite,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = with(LocalDensity.current) { searchBarSize.height.toDp() + 32.dp },
                    end = 16.dp,
                    bottom = 16.dp
                ),
            )
        }

        FabContainer(
            filterList = filterList,
            onFilterListChange = onFilterChange,
            onResetFilter = onResetFilter,
            containerState = filterFabContainerState,
            onContainerStateChange = { newContainerState ->
                filterFabContainerState = newContainerState
            },
            modifier = Modifier
                .zIndex(1f + if (filterFabContainerState == FabContainerState.Fullscreen) 1f else 0f)
                .align(Alignment.BottomEnd)
        )
    }
}

