package com.goudurixx.pokedex.features.home

import DockedSearchContainer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.ui.component.radarChart.DrawerItem
import com.goudurixx.pokedex.core.ui.component.radarChart.ExpandableDrawer
import com.goudurixx.pokedex.features.pokemon.SearchUiState
import com.goudurixx.pokedex.features.pokemon.models.Generations
import com.goudurixx.pokedex.features.pokemon.models.TypeColor

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
    uiState: HomeScreenUiState,
    navigateToPokemonList: () -> Unit,
    navigateToPokemonFavorite: () -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit,
) {
    var searchBarSize by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DockedSearchContainer(
            query = search,
            onSearch = {
                navigateToPokemonResultList(
                    FilterByParameter.NAME,
                    it, //VALUE
                    it, //NAME OF THE SCREEN
                    null
                )
            },
            onQueryChange = onUpdateSearch,
            onClickOnResult = navigateToPokemonDetail,
            modifier = Modifier.onSizeChanged { newSize ->
                if (newSize.height < searchBarSize.height || searchBarSize.height == 0) searchBarSize =
                    newSize
            },
            state = searchState
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = searchBarSize.height.dp)
        ) {
            Text(
                text = "Welcome to Pokedex!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            if (uiState is HomeScreenUiState.Success) Text(
                text = "Home of ${uiState.pokemonCount} pokemons",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedIconButton(onClick = navigateToPokemonList) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "navigate to pokemon list",
                        modifier = Modifier.rotate(90f).fillMaxSize()
                    )
                    Icon(
                        imageVector = Icons.Default.Animation,
                        contentDescription = "pokemon list",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red
                    )
                }
                OutlinedIconButton(onClick = navigateToPokemonFavorite) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "navigate to pokemon favorite",
                        modifier = Modifier.rotate(90f).fillMaxSize()
                    )
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "favorite",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red
                    )
                }
            }
            ExpandableDrawer(
                objectSize = 120.dp,
                drawerTitle = "Types (${TypeColor.entries.size})",
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

            ExpandableDrawer(
                objectSize = 80.dp,
                drawerTitle = "Generations (${Generations.entries.size})",
                itemList = Generations.entries.map {
                    DrawerItem(it.id, it.generationName)
                },
                onItemClick = { id, name, color ->
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