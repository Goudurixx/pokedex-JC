package com.goudurixx.pokedex.features.pokemon

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.ui.component.radarChart.RadarChart
import com.goudurixx.pokedex.core.ui.component.radarChart.models.NetLinesStyle
import com.goudurixx.pokedex.core.ui.component.radarChart.models.Polygon
import com.goudurixx.pokedex.core.ui.component.radarChart.models.PolygonStyle
import com.goudurixx.pokedex.core.ui.theme.PokedexTheme
import com.goudurixx.pokedex.core.ui.theme.TypePsychic
import com.goudurixx.pokedex.core.ui.utils.getContrastingColor
import com.goudurixx.pokedex.features.pokemon.models.EvolutionChainSpeciesUiModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonDetailUiModel
import com.goudurixx.pokedex.features.pokemon.models.SpritesUiModel
import com.goudurixx.pokedex.features.pokemon.models.StatUiModel
import com.goudurixx.pokedex.features.pokemon.models.TypeUiModel
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.min
import kotlin.random.Random

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
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = toolbarAlpha.value.coerceIn(
                            0f,
                            1f
                        )
                    ),
                    navigationIconContentColor = Color(dominantColor).getContrastingColor(),
                    titleContentColor = Color(dominantColor).getContrastingColor()
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

                        var mediaPlayer : MediaPlayer? = null
                        try{
                            mediaPlayer = MediaPlayer().apply {
                                setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
                                )
                                setDataSource(uiState.pokemon.cries)
                                prepareAsync() // might take long! (for buffering, etc)
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }

                        Box(
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {

                            SubcomposeAsyncImage(
                                model = imageRequest,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                val state = painter.state
                                val transition by animateFloatAsState(
                                    targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f,
                                    label = "transition of image"
                                )

                                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                                    CircularProgressIndicator()
                                } else {
                                    SubcomposeAsyncImageContent(
                                        modifier = Modifier
                                            .scale(.8f + (.2f * transition))
                                            .graphicsLayer { rotationX = (1f - transition) * 5f }
                                            .alpha(min(1f, transition / .2f))

                                    )
                                }
                            }

                            OutlinedIconButton(
                                onClick = { mediaPlayer?.start() },
                                modifier = Modifier.align(Alignment.BottomEnd),
                                colors = IconButtonDefaults.outlinedIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                        alpha = 0.5f
                                    ),
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
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
//                                .heightIn(min = 150.dp, max = 300.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        StatisticsContentAsRadarChart(
                            stats = uiState.pokemon.stats,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 150.dp, max = 300.dp)
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

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = "Types",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            items(types) { (_, type, color) ->
                Button(
                    onClick = { /*TODO navigate to type screen*/ },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(32.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = color,
                        contentColor = color.getContrastingColor()
                    )
                ) {
                    Text(
                        text = type.capitalize(Locale.ROOT),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticsContent(stats: List<StatUiModel>, modifier: Modifier) {
    var beginAnimation by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(100L + Random.nextInt(500))
        beginAnimation = true
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            for (stat in stats) {
                val transition by animateFloatAsState(
                    targetValue = if (beginAnimation) stat.value / 200f else 0f,
                    label = "transition of the statistics ${stat.statId}"
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stat.statName.capitalize(Locale.ROOT),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth(4 / 12f)
//                            .padding(8.dp)
                    )
                    Text(
                        text = stat.value.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth(3 / 12f)
                            .padding(horizontal = 8.dp)
                    )
                    LinearProgressIndicator(
                        progress = { transition },
                        trackColor = Color.LightGray,
                        color = stat.color,
                        modifier = Modifier
//                            .rotate(90f)
                            .fillMaxWidth(1f)
//                            .padding(8.dp)
                        ,
                        strokeCap = StrokeCap.Round
                    )

                }
            }
        }
    }
}

@Composable
fun StatisticsContentAsRadarChart(stats: List<StatUiModel>, modifier: Modifier) {
    var beginAnimation by rememberSaveable {
        mutableStateOf(false)
    }

    //TODO MOVE THAT IN CALL AT APP LAUNCH
    val averageStat = listOf<Double>(71.27, 81.58, 75.24, 73.65, 73.00, 71.16)

    LaunchedEffect(Unit) {
        delay(100L + Random.nextInt(500))
        beginAnimation = true
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        RadarChart(
            radarLabels = stats.map {
                it.statName.capitalize(Locale.ROOT) + "\n" + String.format(
                    "%0${3}d",
                    it.value
                )
            },
            labelsStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center),
            netLinesStyle = NetLinesStyle(
                netLineColor = MaterialTheme.colorScheme.tertiary,
                netLinesStrokeWidth = 2f,
                netLinesStrokeCap = StrokeCap.Butt
            ),
            scalarSteps = 2,
            scalarValue = 200.0,
            scalarValuesStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Transparent),
            polygons = listOf(
                Polygon(
                    values = averageStat,
                    unit = "",
                    style = PolygonStyle(
                        fillColor = MaterialTheme.colorScheme.secondary,
                        fillColorAlpha = 0.1f,
                        borderColor = MaterialTheme.colorScheme.errorContainer,
                        borderColorAlpha = 0.5f,
                        borderStrokeWidth = 1f,
                        borderStrokeCap = StrokeCap.Butt
                    )
                ),
                Polygon(
                    values = stats.map { it.value.toDouble() },
                    unit = "",
                    style = PolygonStyle(
                        fillColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        fillColorAlpha = 0.5f,
                        borderColor = MaterialTheme.colorScheme.surfaceTint,
                        borderColorAlpha = 0.5f,
                        borderStrokeWidth = 2f,
                        borderStrokeCap = StrokeCap.Butt
                    )
                )
            ),
            modifier = Modifier.fillMaxSize()
        )
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
        onClick = { onCardClick(dominantColor) },
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
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
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

@Preview
@Composable
fun PokemonDetailScreenPreview() {
    CompositionLocalProvider(LocalContext provides LocalContext.current) {
        PokedexTheme {
            val title by remember {
                mutableStateOf("Pachyradjah N°0879")
            }
            val backgroundColor = MaterialTheme.colorScheme.secondaryContainer
            val dominantColor by remember { mutableIntStateOf(backgroundColor.toArgb()) }

            val statList = listOf(
                Pair("PV", 92),
                Pair("Attaque", 130),
                Pair("Défense", 115),
                Pair("Attaque Spéciale", 80),
                Pair("Défense Spéciale", 85),
                Pair("Vitesse", 55)
            )

            val pokemonDetail = PokemonDetailUiModel(
                id = 879,
                name = "pachyradjah",
                weight = 650,
                height = 3,
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/879.png",
                cries = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/cries/879.mp3",
                types = listOf(
                    TypeUiModel(id = 1, name = "Psychic", color = TypePsychic),
                ),
                stats = statList.map { (name, value) ->
                    StatUiModel(
                        statName = name,
                        statId = statList.indexOfFirst { it.first == name } + 1,
                        value = value,
                        effort = 0,
                        color = when (name) {
                            "PV" -> Color(0xFFA8A878)
                            "Attaque" -> Color(0xFFEE8130)
                            "Défense" -> Color(0xFF6390F0)
                            "Attaque Spéciale" -> Color(0xFFF7D02C)
                            "Défense Spéciale" -> Color(0xFF96D9D6)
                            "Vitesse" -> Color(0xFFC5C5C5)
                            else -> Color.Black
                        }
                    )
                },
                sprites = SpritesUiModel(),
            )
            PokemonDetailScreen(
                uiState = PokemonDetailUiState.Success(pokemonDetail),
                speciesUiState = PokemonSpeciesUiState.Loading,
                onBackClick = { /*TODO*/ },
                navigateToPokemonDetail = { _, _ -> },
                backgroundColor = dominantColor
            )
        }
    }
}

