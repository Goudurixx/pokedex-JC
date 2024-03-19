package com.goudurixx.pokedex.features.home

import DockedSearchContainer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goudurixx.pokedex.features.pokemon.SearchUiState
import com.goudurixx.pokedex.features.pokemon.models.TypeColor

@Composable
fun HomeRoute(
    navigateToPokemonList: () -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    navigateToType: (Int, String) -> Unit,
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
        navigateToType = navigateToType,
    )
}

@Composable
fun HomeScreen(
    search: String,
    searchState: SearchUiState,
    onUpdateSearch: (String) -> Unit,
    uiState: HomeScreenUiState,
    navigateToPokemonList: () -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    navigateToType: (Int, String) -> Unit,
) {
    var searchBarSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DockedSearchContainer(
                query = search,
                onQueryChange = onUpdateSearch,
                onClickOnResult = navigateToPokemonDetail,
                modifier = Modifier.onSizeChanged { newSize ->
                    if (newSize.height < searchBarSize.height || searchBarSize.height == 0) searchBarSize =
                        newSize
                },
                state = searchState
            )
            Text(
                text = "Home Screen - Welcome to Pokedex!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            if (uiState is HomeScreenUiState.Success) Text(text = "Home of ${uiState.pokemonCount} pokemons!")
            OutlinedButton(onClick = navigateToPokemonList) {
                Text(text = "Go to Pokemon List")
            }
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Adaptive(32.dp),
                modifier = Modifier
                    .heightIn(max = 120.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(8.dp)
                    ),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(TypeColor.entries) {
                    OutlinedButton(
                        onClick = { navigateToType(it.id, it.name) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = it.color
                        ),
                        modifier = Modifier.padding(4.dp).fillMaxHeight(0.5f)
                    ) {
                        Text(text = it.name)
                    }
                }
            }
        }
    }
}