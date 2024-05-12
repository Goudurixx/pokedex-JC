package com.goudurixx.pokedex.features.home

import DockedSearchContainer
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.ui.component.radarChart.DrawerItem
import com.goudurixx.pokedex.core.ui.component.radarChart.ExpandableDrawer
import com.goudurixx.pokedex.features.pokemon.SearchUiState
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.TypeColor
import java.util.Date

@Composable
fun HomeRoute(
    navigateToPokemonList: () -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    navigateToPokemonFavorite: () -> Unit,
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val search by viewModel.search.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    HomeScreen(
        search = search,
        searchState = searchState,
        onUpdateSearch = viewModel::updateSearch,
        onUpdateAppData = viewModel::updateAppData,
        uiState = uiState,
        navigateToPokemonList = navigateToPokemonList,
        navigateToPokemonDetail = navigateToPokemonDetail,
        navigateToPokemonResultList = navigateToPokemonResultList,
        navigateToPokemonFavorite = navigateToPokemonFavorite,
    )
}

@Composable
fun HomeScreen(
    search: String,
    searchState: SearchUiState,
    onUpdateSearch: (String) -> Unit,
    onUpdateAppData: () -> Unit,
    uiState: HomeScreenUiState,
    navigateToPokemonList: () -> Unit,
    navigateToPokemonFavorite: () -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit,
) {
    var searchBarSize by remember { mutableStateOf(IntSize.Zero) }
    LaunchedEffect(searchBarSize) {
        Log.e("HomeScreen", "searchBarSize = $searchBarSize")
    }
    val backgroundColor = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DockedSearchContainer(
            query = search,
            onSearch = {
                navigateToPokemonResultList(
                    FilterByParameter.NAME,
                    it, //VALUE
                    "name contains : $it", //NAME OF THE SCREEN
                    null
                )
            },
            onQueryChange = onUpdateSearch,
            onClickOnResult = navigateToPokemonDetail,
            modifier = Modifier
                .drawBehind {
                    val brush = Brush.verticalGradient(
                        0.0f to backgroundColor,
                        1.0f to backgroundColor.copy(alpha = 0.0f)
                    )
                    drawRect(brush = brush)
                }
                .onSizeChanged { newSize ->
                    if (newSize.height < searchBarSize.height || searchBarSize.height == 0) searchBarSize =
                        newSize
                },
            state = searchState
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = with(LocalDensity.current) { searchBarSize.height.toDp() + 16.dp })
        ) {

            HomeCard(
                uiState = uiState,
                onUpdateAppData = onUpdateAppData
            )

            if (favoriteUiState is FavoriteUiState.Success) {
                FavoriteCard(
                    favoriteUiState.favoritePokemon,
                    navigateToPokemonDetail,
                    navigateToPokemonFavorite,
                    navigateToPokemonList
                )
            }
            ExpandableDrawer(
                objectSize = 120.dp,
                drawerTitle = "Types",
                itemList = TypeColor.entries.map {
                    DrawerItem(it.id, it.name, it.color)
                },
                onItemClick = { id, name, color ->
                    navigateToPokemonResultList(
                        FilterByParameter.TYPE,
                        id.toString(),
                        name,
                        color?.toArgb()
                    )
                }
            )

            if (uiState is HomeScreenUiState.Success || uiState is HomeScreenUiState.SuccessReloading) {
                ExpandableDrawer(
                    objectSize = 80.dp,
                    drawerTitle = "Generations",
                    itemList =
                    when (uiState) {
                        is HomeScreenUiState.Success -> uiState.generationList.map {
                            DrawerItem(it.id, it.name)
                        }

                        is HomeScreenUiState.SuccessReloading -> uiState.generationList.map {
                            DrawerItem(it.id, it.name)
                        }

                        else -> emptyList()
                    },
                    onItemClick = { id, name, color ->
                        Log.e("HomeScreen", "Generation $name, id = $id color = $color")
                        navigateToPokemonResultList(
                            FilterByParameter.GENERATION,
                            id.toString(),
                            "Generation $name",
                            color?.toArgb()
                        )
                    }
                )
            }
        }
    }
}