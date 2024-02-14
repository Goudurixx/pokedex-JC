package com.goudurixx.pokedex.features.pokemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel

@Composable
fun PokemonListRoute(
    navigateToPokemonDetail: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val pokemonLazyPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()
    PokemonListScreen(
        pokemonLazyPagingItems = pokemonLazyPagingItems,
        navigateToPokemonDetail = navigateToPokemonDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    navigateToPokemonDetail: (Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Blue
                    )
                )
            )
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.pokedex_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(16.dp)
                    .height(120.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            DockedSearchBar(
                query = "SearchPokemonHere",
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {}
            LazyColumn {
                items(
                    count = pokemonLazyPagingItems.itemCount,
                    key = pokemonLazyPagingItems.itemKey { pokemon -> pokemon.id },
                    contentType = pokemonLazyPagingItems.itemContentType { "Pokemon" }
                ) { index: Int ->
                    val pokemon: PokemonListItemUiModel? = pokemonLazyPagingItems[index]
                    if (pokemon != null) {
                        PokemonListItem(
                            pokemon = pokemon,
                            navigateToPokemonDetail = navigateToPokemonDetail
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonListItem(pokemon: PokemonListItemUiModel, navigateToPokemonDetail: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navigateToPokemonDetail(pokemon.id) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(pokemon.imageUrl).crossfade(true)
            .build()

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )
            Text(text = pokemon.name)
        }
    }
}