package com.goudurixx.pokedex.features.pokemon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PokemonDetailRoute(
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PokemonDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer.toArgb()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    uiState: PokemonDetailUiState,
    onBackClick: () -> Unit,
    backgroundColor: Int
) {
    var title by remember {
        mutableStateOf("")
    }
    var dominantColor by remember { mutableIntStateOf(backgroundColor) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = title) }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
        },
        containerColor = Color(dominantColor),
        contentColor = Color(dominantColor).takeIf { it.luminance() > 0.5f }
            ?.let { Color.Black } ?: Color.White
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (uiState) {
                PokemonDetailUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                is PokemonDetailUiState.Error -> {
                    Text(text = "there was an error")
                }

                is PokemonDetailUiState.Success -> {
                    val imageRequest = ImageRequest.Builder(LocalContext.current)
                        .data(uiState.pokemon.imageUrl)
                        .allowHardware(false)
                        .crossfade(true)
                        .listener { _, result ->
                            result.drawable.let {
                                dominantColor =
                                    Palette.from(it.toBitmap()).generate().dominantSwatch?.rgb
                                        ?: backgroundColor
                            }

                        }.build()
                    title = uiState.pokemon.name
                    AsyncImage(
                        model = imageRequest,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                    )
                    Text(text = uiState.pokemon.name)
                }
            }
        }
    }
}