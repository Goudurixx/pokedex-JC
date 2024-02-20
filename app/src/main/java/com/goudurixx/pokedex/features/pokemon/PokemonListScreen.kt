package com.goudurixx.pokedex.features.pokemon

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
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
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import java.util.Locale

@Composable
fun PokemonListRoute(
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val pokemonLazyPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()

    val search by viewModel.search.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    PokemonListScreen(
        search = search,
        searchState = searchState,
        onUpdateSearch = viewModel::updateSearch,
        pokemonLazyPagingItems = pokemonLazyPagingItems,
        navigateToPokemonDetail = navigateToPokemonDetail
    )
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    search: String,
    searchState: SearchUiState,
    onUpdateSearch: (String) -> Unit,
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    navigateToPokemonDetail: (Int, Int) -> Unit
) {
    var active by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val backgroundColor = MaterialTheme.colorScheme.background
    Scaffold(
//        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                active = false
                keyboardController?.hide()
            }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Image(
                painter = painterResource(id = R.drawable.pokedex_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(16.dp)
                    .height(64.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            )
            Box(Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .semantics { isTraversalGroup = true }
                        .zIndex(1f)
                        .fillMaxWidth()
                        .drawBehind {
                            val brush = Brush.verticalGradient(
                                0.0f to backgroundColor,
                                1.0f to backgroundColor.copy(alpha = 0.0f)
                            )
                            drawRect(brush = brush)
                        }
                ) {
                    DockedSearchBar(
                        query = search,
                        onQueryChange = onUpdateSearch,
                        onSearch = {
                            active = false
                            keyboardController?.hide()
                            if (searchState is SearchUiState.Success && searchState.list.isNotEmpty()) navigateToPokemonDetail(
                                searchState.list.first().id,
                                backgroundColor.toArgb()
                            )
                        },
                        active = active,
                        onActiveChange = { active = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        placeholder = { Text("Search pokemon name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }, shadowElevation = 16.dp,
                        trailingIcon = {
                            IconButton(onClick = { onUpdateSearch("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null)
                            }
                        }
                    ) {
                        if (searchState is SearchUiState.Success)
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(searchState.list) { pokemon ->
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                text = pokemon.name.capitalize(Locale.ROOT),
                                            )
                                        },
                                        overlineContent = {
                                            Text(
                                                text = pokemon.id.toString(),
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                navigateToPokemonDetail(
                                                    pokemon.id,
                                                    backgroundColor.toArgb()
                                                )
                                            }
                                    )
                                }
                            }
                    }
                }
                PokemonList(
                    pokemonLazyPagingItems = pokemonLazyPagingItems,
                    onItemClick = navigateToPokemonDetail,
                )
            }
        }
    }
}

@Composable
private fun PokemonList(
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    onItemClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = state,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 80.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = pokemonLazyPagingItems.itemCount,
            key = pokemonLazyPagingItems.itemKey { pokemon -> pokemon.id },
            contentType = pokemonLazyPagingItems.itemContentType { "Pokemon" }
        ) { index: Int ->

            val pokemon: PokemonListItemUiModel? = pokemonLazyPagingItems[index]
            if (pokemon != null) {
                PokemonListItem(
                    pokemon = pokemon,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer.toArgb(),
                    onItemClick = onItemClick,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun PokemonListItem(
    pokemon: PokemonListItemUiModel,
    backgroundColor: Int,
    onItemClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dominantColor by remember { mutableIntStateOf(backgroundColor) }


    Card(
        onClick = { onItemClick(pokemon.id, dominantColor) },
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(dominantColor),
            contentColor = Color(dominantColor).takeIf { it.luminance() > 0.5f }
                ?.let { Color.Black } ?: Color.White
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
            Column(modifier = Modifier.fillMaxWidth()){
                Text(text = String.format("%0${5}d", pokemon.id), style = MaterialTheme.typography.bodySmall)
                Text(text = pokemon.name.capitalize(Locale.ROOT), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}