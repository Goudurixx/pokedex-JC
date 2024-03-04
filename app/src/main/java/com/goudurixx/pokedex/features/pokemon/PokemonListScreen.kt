package com.goudurixx.pokedex.features.pokemon

import DockedSearchContainer
import FabContainer
import FabContainerState
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.common.models.OrderByParameter
import com.goudurixx.pokedex.core.common.models.OrderByValues
import com.goudurixx.pokedex.core.ui.utils.getContrastingColor
import com.goudurixx.pokedex.features.pokemon.models.BaseFilterItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PokemonListRoute(
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
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
        pokemonLazyPagingItems = pokemonLazyPagingItems,
        navigateToPokemonDetail = navigateToPokemonDetail
    )
}

@SuppressLint("RememberReturnType")
@Composable
fun PokemonListScreen(
    search: String,
    sortFilterList: List<SortOrderItem>,
    filterList : List<BaseFilterItemUiModel>,
    searchState: SearchUiState,
    onUpdateSearch: (String) -> Unit,
    onUpdateSort: (SortOrderItem) -> Unit,
    onFilterChange : (List<BaseFilterItemUiModel>) -> Unit,
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    navigateToPokemonDetail: (Int, Int) -> Unit
) {

    var filterFabContainerState by remember { mutableStateOf(FabContainerState.Fab) }

    val selectedFilter by remember(sortFilterList) {
        derivedStateOf {
            sortFilterList.firstOrNull { it.order != null }
        }
    }
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showScrollToTopButton by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 5
        }
    }

    Box(
        Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        DockedSearchContainer(
            sortFilterList = sortFilterList,
            query = search,
            onQueryChange = onUpdateSearch ,
            onFilterClick = { sortFilterItem ->
                when (sortFilterItem.order) {
                    null -> onUpdateSort(sortFilterItem.copy(order = OrderByValues.ASC))
                    OrderByValues.ASC -> onUpdateSort(sortFilterItem.copy(order = OrderByValues.DESC))
                    OrderByValues.DESC -> onUpdateSort(sortFilterItem.copy(order = null))
                }
                pokemonLazyPagingItems.refresh()
            },
            onClickOnResult = navigateToPokemonDetail,
            state = searchState
        )
        PokemonList(
            selectedFilter = selectedFilter,
            pokemonLazyPagingItems = pokemonLazyPagingItems,
            enabled = filterFabContainerState == FabContainerState.Fab,
            onItemClick = navigateToPokemonDetail,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 88.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            state = state
        )
        AnimatedVisibility(
            visible = showScrollToTopButton,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier.padding(top = 64.dp)
        ) {
            FloatingActionButton(
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    scope.launch {
                        state.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
        FabContainer(
            filterList = filterList,
            onFilterListChange = onFilterChange ,
            containerState = filterFabContainerState,
            onContainerStateChange = { newContainerState ->
                filterFabContainerState = newContainerState
            },
            modifier = Modifier
                .zIndex(1f + if(filterFabContainerState == FabContainerState.Fullscreen) 1f else 0f)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun PokemonList(
    selectedFilter: SortOrderItem?,
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    onItemClick: (Int, Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: LazyListState = rememberLazyListState(),
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
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
                    modifier = Modifier
                )
            }
        }
        item {
            if (pokemonLazyPagingItems.loadState.append.endOfPaginationReached) {
                Text(
                    text = "No more pokemons to load",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}

@Composable
fun PokemonListItem(
    pokemon: PokemonListItemUiModel,
    enabled: Boolean,
    backgroundColor: Int,
    selectedFilter: SortOrderItem?,
    onItemClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dominantColor by remember { mutableIntStateOf(backgroundColor) }


    Card(
        onClick = { onItemClick(pokemon.id, dominantColor) },
        modifier = modifier
            .fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(dominantColor),
            contentColor = Color(dominantColor).getContrastingColor()
        ),
    ) {

        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(pokemon.imageUrl)
            .allowHardware(false)
            .crossfade(true)
            .listener { _, result ->
                result.drawable.let {
                    dominantColor = Palette.from(it.toBitmap()).generate().dominantSwatch?.rgb
                        ?: backgroundColor
                }

            }
            .placeholder(R.drawable.pokeball)
            .build()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .drawWithContent {
                        drawCircle(
                            color = Color.Black,
                            blendMode = BlendMode.Softlight
                        )
                        drawContent()
                    }
                    .padding(8.dp)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = String.format("#%0${3}d", pokemon.id),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = pokemon.name.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.bodyLarge
                )
                when (selectedFilter?.parameter) {
                    OrderByParameter.HEIGHT -> "${pokemon.height} m"
                    OrderByParameter.WEIGHT -> "${pokemon.weight} kg"
                    OrderByParameter.BASE_EXPERIENCE -> "${pokemon.baseExperience ?: 0} EXP"
                    OrderByParameter.AVERAGE_STATS -> "${pokemon.averageStat?.roundToInt() ?: 0}"
                    else -> null
                }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}