package com.goudurixx.pokedex.features.pokemon

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.ui.theme.PokedexTheme
import com.goudurixx.pokedex.features.pokemon.models.EvolutionChainSpeciesUiModel
import com.goudurixx.pokedex.features.pokemon.models.StatUiModel
import com.goudurixx.pokedex.features.pokemon.models.TypeUiModel
import java.util.Locale

@Composable
fun PokemonDetailRoute(
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (Int, Int?) -> Unit,
    backgroundColor: Int? = null,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val speciesUiState by viewModel.speciesUiState.collectAsStateWithLifecycle()
    PokemonDetailScreen(
        uiState = uiState,
        speciesUiState = speciesUiState,
        onBackClick = onBackClick,
        navigateToPokemonDetail = navigateToPokemonDetail,
        backgroundColor = backgroundColor ?: MaterialTheme.colorScheme.primary.toArgb()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    uiState: PokemonDetailUiState,
    speciesUiState: PokemonSpeciesUiState,
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (Int, Int?) -> Unit,
    backgroundColor: Int
) {
    var title by remember {
        mutableStateOf("")
    }
    var dominantColor by remember { mutableIntStateOf(backgroundColor) }
    val lazyListState = rememberLazyListState()
    val toolbarAlpha = remember {
        derivedStateOf {
            // Calculate the alpha of the toolbar based on the scroll position
            // The higher the divisor, the slower the toolbar will become opaque as you scroll
            lazyListState.firstVisibleItemScrollOffset.toFloat() / 100
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = toolbarAlpha.value.coerceIn(0f, 1f)),
                )
            )
        },
        containerColor = Color(dominantColor),
        contentColor = Color(dominantColor).takeIf { it.luminance() > 0.5f }
            ?.let { Color.Black } ?: Color.White
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            state = lazyListState,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                PokemonDetailUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }
                    }
                }

                is PokemonDetailUiState.Error -> {
                    item {
                        Text(text = "there was an error : ")
                        Text(text = uiState.error.message ?: "Unknown error")
                    }
                }

                is PokemonDetailUiState.Success -> {
                    item {
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
                        title = stringResource(
                            R.string.pokemon_detail_screen_title,
                            uiState.pokemon.name.capitalize(Locale.ROOT),
                            uiState.pokemon.id
                        )

                        val mediaPlayer = MediaPlayer().apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                            )
                            setDataSource(uiState.pokemon.cries)
                            prepareAsync() // might take long! (for buffering, etc)
                        }

                        Box(
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            AsyncImage(
                                model = imageRequest,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )

                            OutlinedIconButton(
                                onClick = { mediaPlayer.start() },
                                modifier = Modifier.align(Alignment.BottomEnd),
                                colors = IconButtonDefaults.outlinedIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                        alpha = 0.5f
                                    ),
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {
                                Icon(
                                    imageVector = if (mediaPlayer.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    item {
                        TypesContent(
                            types = uiState.pokemon.types,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 200.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        StatisticsContent(
                            stats = uiState.pokemon.stats,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 150.dp, max = 200.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        EvolutionChainContent(
                            speciesUiState = speciesUiState,
                            navigateToPokemonDetail = navigateToPokemonDetail,
                            modifier = Modifier
                                .heightIn(min = 150.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
fun TypesContent(types: List<TypeUiModel>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        item {
            Text(
                text = "Types",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)
        }
        items(types) { (_, type, color) ->
            Card(
                onClick = { /* travel to selected type */},
                modifier = Modifier
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = color),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 16.dp,
                    pressedElevation = 8.dp
                ),
            ) {
                Text(
                    text = type.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(32.dp)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun StatisticsContent(stats: List<StatUiModel>, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Row {
            for (stat in stats) {
                val textMeasurer = rememberTextMeasurer()
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(200.dp)
                        .weight(1f / stats.size),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider(
                        modifier = Modifier
                            .height(150.dp)
                            .width(20.dp)
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    color = Color.White,
                                    size = size.copy(
                                        width = this.size.width,
                                        height = this.size.height * (1 - stat.value / 200f)
                                    )
                                )
                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = stat.value.toString(),
                                    topLeft = Offset(
                                        this.size.width,
                                        this.size.height * (1 - stat.value / 200f)
                                    ),
                                    size = this.size.copy(
                                        width = this.size.width * 2,
                                        height = this.size.height
                                    ),
                                )
                            }
                            .background(Color.Black)
                    )
                    Text(
                        text = stat.statName,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun EvolutionChainContent(
    speciesUiState: PokemonSpeciesUiState,
    navigateToPokemonDetail: (Int, Int?) -> Unit,
    modifier: Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        when (speciesUiState) {
            PokemonSpeciesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is PokemonSpeciesUiState.Success -> {
                val firstPokemon =
                    speciesUiState.species.evolutionChain.values.find { it.evolveFromId == null }
                if (firstPokemon != null) {
                    EvolutionChainRow(
                        evolutionChain = speciesUiState.species.evolutionChain,
                        currentPokemon = firstPokemon,
                        onPokemonClick = navigateToPokemonDetail,
                        modifier = Modifier
                            .heightIn(min = 150.dp)
                            .fillMaxWidth()
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
fun EvolutionChainRow(
    evolutionChain: Map<Int, EvolutionChainSpeciesUiModel>,
    currentPokemon: EvolutionChainSpeciesUiModel,
    onPokemonClick: (Int, Int?) -> Unit,
    modifier: Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PokemonCard(
            pokemon = currentPokemon,
            onCardClick = { color -> onPokemonClick(currentPokemon.id, color) })
        // Draw arrow between Pokemon
        if (evolutionChain.values.any { it.evolveFromId == currentPokemon.id })
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null
            )

        Column {
            evolutionChain.forEach {
                if (it.value.evolveFromId == currentPokemon.id) {

                    EvolutionChainRow(
                        evolutionChain = evolutionChain,
                        currentPokemon = it.value,
                        onPokemonClick = onPokemonClick,
                        modifier = Modifier.heightIn(min = 150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: EvolutionChainSpeciesUiModel, onCardClick: (Int?) -> Unit) {

    val surface = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableIntStateOf(surface.toArgb()) }

    Card(
        onClick = { onCardClick(dominantColor)},
        modifier = Modifier
            .padding(8.dp)
            .width(80.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(dominantColor),
            contentColor = Color(dominantColor).takeIf { it.luminance() > 0.5f }
                ?.let { Color.Black } ?: Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.imageUrl)
                .allowHardware(false)
                .crossfade(true)
                .listener { _, result ->
                    result.drawable.let {
                        dominantColor =
                            Palette.from(it.toBitmap()).generate().dominantSwatch?.rgb
                                ?: surface.toArgb()
                    }
                }.build()

            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Text(
                text = pokemon.name.capitalize(Locale.ROOT),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PokemonDetailScreenPreview() {
    PokedexTheme {
        val title by remember {
            mutableStateOf("Pachyradjah N°0879")
        }
        val backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        val dominantColor by remember { mutableIntStateOf(backgroundColor.toArgb()) }
        val detailList = listOf(
            Pair("Taille", "3,0 m"),
            Pair("Catégorie", "Pachycuivre"),
            Pair("Poids", "650,0 kg"),
            Pair("Talent", "Sans Limite"),
            Pair("Sexe", "${Char(9792)} ${Char(9794)}"),
        )

        val statList = listOf(
            Pair("PV", 92),
            Pair("Attaque", 130),
            Pair("Défense", 115),
            Pair("Attaque Spéciale", 80),
            Pair("Défense Spéciale", 85),
            Pair("Vitesse", 55)
        )
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(title = { Text(text = title) }, navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                })
            },
            containerColor = Color(dominantColor),
            contentColor = Color(dominantColor).takeIf { it.luminance() > 0.5f }
                ?.let { Color.Black } ?: Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(8.dp)
            ) {

                Image(painterResource(id = R.drawable.pokeball), contentDescription = null)
                Text(text = "Ce Pokémon est originaire d’une région lointaine. On l’a amené à Paldea il y a bien longtemps. Il est si fort qu’il peut facilement tirer un avion.")

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                ) {
                    items(detailList) { (title, value) ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = title, style = MaterialTheme.typography.bodySmall)
                            Text(text = value, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                ) {
                    Row {
                        for (stat in statList) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(200.dp)
                                    .weight(1f / statList.size),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Divider(modifier = Modifier
                                    .height(150.dp)
                                    .width(20.dp)
                                    .drawWithContent {
                                        drawContent()
                                        drawRect(
                                            color = Color.Black,
                                            size = size.copy(
                                                this.size.width,
                                                this.size.height * (1 - stat.second / 255f)
                                            )
                                        )
                                    })
                                Text(text = stat.first, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

