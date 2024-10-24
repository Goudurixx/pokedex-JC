package com.goudurixx.pokedex.features.pokemon

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pentagon
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.common.models.GlobalStatList
import com.goudurixx.pokedex.core.ui.component.radarChart.RadarChart
import com.goudurixx.pokedex.core.ui.component.radarChart.models.NetLinesStyle
import com.goudurixx.pokedex.core.ui.component.radarChart.models.Polygon
import com.goudurixx.pokedex.core.ui.component.radarChart.models.PolygonStyle
import com.goudurixx.pokedex.core.ui.utils.getContrastingColor
import com.goudurixx.pokedex.core.ui.utils.shimmerEffect
import com.goudurixx.pokedex.features.pokemon.models.EvolutionChainSpeciesUiModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonDetailUiModel
import com.goudurixx.pokedex.features.pokemon.models.StatUiModel
import com.goudurixx.pokedex.features.pokemon.models.TypeUiModel
import java.util.Locale
import kotlin.math.min

@Composable
fun PokemonDetailRoute(
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (Int, Int?) -> Unit,
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit,
    pokemonId: Int? = null,
    backgroundColor: Int? = null,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appDataUiState by viewModel.appDataUiState.collectAsStateWithLifecycle()
    val speciesUiState by viewModel.speciesUiState.collectAsStateWithLifecycle()

    BackHandler {
        onBackClick()
    }
    PokemonDetailScreen(
        uiState = uiState,
        appDataUiState = appDataUiState,
        speciesUiState = speciesUiState,
        updateFavorite = viewModel::updateFavorite,
        onBackClick = onBackClick,
        navigateToPokemonDetail = navigateToPokemonDetail,
        navigateToPokemonResultList = navigateToPokemonResultList,
        pokemonId = pokemonId,
        backgroundColor = backgroundColor ?: MaterialTheme.colorScheme.primary.toArgb()
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationGraphicsApi::class
)
@Composable
fun PokemonDetailScreen(
    uiState: PokemonDetailUiState,
    appDataUiState: AppDataUiState,
    speciesUiState: PokemonSpeciesUiState,
    updateFavorite: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (Int, Int?) -> Unit,
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit,
    pokemonId: Int?,
    backgroundColor: Int,
) {
    var pokemon by remember {
        mutableStateOf(
            PokemonDetailUiModel.placeHolder(id = pokemonId, color = backgroundColor)
        )
    }
    val dominantColor by rememberSaveable { mutableIntStateOf(pokemon.color.color.toArgb()) }
    val columnBackgroundColor = MaterialTheme.colorScheme.surface
    val scrollState = rememberScrollState()
    val toolbarAlpha = remember {
        derivedStateOf {
            // Calculate the alpha of the toolbar based on the scroll position
            // The higher the divisor, the slower the toolbar will become opaque as you scroll
            scrollState.value.toFloat() / 100
        }
    }
    val isFullyVisible by remember(uiState) {
        derivedStateOf<Boolean> {
            uiState is PokemonDetailUiState.Success
        }
    }
    val pokebalImageVector = ImageVector.vectorResource(R.drawable.pokeball)
    var atEnd by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(dominantColor)),
        contentAlignment = Alignment.TopEnd
    ) {
        LaunchedEffect(Unit) {
            atEnd = !atEnd
        }
        val loadingImage =
            AnimatedImageVector.animatedVectorResource(R.drawable.pokeball_loader)
        Image(
            rememberAnimatedVectorPainter(loadingImage, atEnd),
            null,
            modifier = Modifier
                .alpha(0.2f)
                .fillMaxWidth(0.5f)
        )
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
            },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = pokemon.name,
                        modifier = Modifier.basicMarquee(),
                        maxLines = 1,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            shadow = Shadow(
                                color = MaterialTheme.colorScheme.onSurface,
                                offset = Offset(0f, 4.0f),
                                blurRadius = 4f
                            ), fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    Text(
                        text = String.format("#%0${3}d", pokemonId),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        updateFavorite(!pokemon.isFavorite)
                    }) {
                        //TODO FIX ICON GETTING STUCK IN ONE STATE
//                        AnimatedContent(
//                            targetState = pokemon,
//                            contentKey = { state ->
//                                // Define a mapping between `targetState` and a key.
//                                // This function should return a key that represents the parts of `targetState` that should trigger a transition.
//                                // For example, if `targetState` is a data class with two properties and you only want to trigger a transition when the first property changes, you can return the first property as the key.
//                                state.isFavorite
//                            },
//                            label = ""
//                        ) { target ->
                        if (pokemon.isFavorite) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.FavoriteBorder,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }

//                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                    containerColor = Color(dominantColor).getContrastingColor()
                        .getContrastingColor().copy(
                            alpha = toolbarAlpha.value.coerceIn(
                                0f,
                                1f
                            )
                        ),
                    navigationIconContentColor = Color(dominantColor).getContrastingColor(),
                    titleContentColor = Color(dominantColor).getContrastingColor(),
                    actionIconContentColor = Color(dominantColor).getContrastingColor()
                )
            )
        },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {

        LaunchedEffect(uiState) {
            if (uiState is PokemonDetailUiState.Success) {
                pokemon = uiState.pokemon
            }
            if (uiState is PokemonDetailUiState.FallbackSuccess) {
                pokemon = uiState.pokemon
            }
        }
        Box(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 4.dp)
                .fillMaxSize()
                .verticalScroll(state = scrollState),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp, bottom = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(columnBackgroundColor, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                AboutContent(
                    pokemon = pokemon,
                    isFullyVisible = isFullyVisible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                AnimatedVisibility(visible = isFullyVisible, modifier = Modifier.fillMaxWidth()) {
                    pokemon.types?.let { types ->
                        TypesContent(
                            types = types,
                            onTypeClicked = { typeId, typeName, typeColor ->
                                navigateToPokemonResultList(
                                    FilterByParameter.TYPE,
                                    typeId.toString(),
                                    typeName,
                                    typeColor
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp)
                        )
                    }
                }
                AnimatedVisibility(visible = isFullyVisible, modifier = Modifier.fillMaxWidth()) {
                    pokemon.stats?.let { stats ->
                        StatisticsContentWrapper(
                            stats = stats,
                            appDataUiState = appDataUiState,
                            color = pokemon.color.color,
                            modifier = Modifier
                                .heightIn(min = 200.dp, max = 300.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                EvolutionChainContent(
                    speciesUiState = speciesUiState,
                    navigateToPokemonDetail = navigateToPokemonDetail,
                    modifier = Modifier
                        .heightIn(min = 150.dp)
                        .fillMaxWidth()
                )

            }
            PokemonImageContent(
                pokemon = pokemon,
            )
        }
        AnimatedVisibility(visible = uiState is PokemonDetailUiState.Error, enter = fadeIn()) {
            PokemonDetailScreenErrorContent(modifier = Modifier.padding(it))
        }
    }
}

@Composable
private fun PokemonDetailScreenErrorContent(modifier: Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
    ) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                }
            }
            .build()
        Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.drawable.team_rocket_error)
                        .apply(block = {
                            size(Size.ORIGINAL)
                        }).build(), imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "Team Rocket has stolen the data",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onError
            )

        }
    }
}

@Composable
fun PokemonImageContent(
    pokemon: PokemonDetailUiModel,
    modifier: Modifier = Modifier
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(pokemon.imageUrl)
        .allowHardware(false)
        .crossfade(true)
//        .listener { _, result ->
//            onDominantColorChange(
//                Palette.from(result.drawable.toBitmap()).generate().dominantSwatch?.rgb
//                    ?: backgroundColor
//            )
//        }
        .build()


    var mediaPlayer: MediaPlayer? = null

    pokemon.cries?.let { cries ->
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(cries)
                prepareAsync() // might take long! (for buffering, etc)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Box(
        modifier = modifier
            .height(250.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        SubcomposeAsyncImage(
            model = imageRequest,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        ) {
            val state = painter.state
            val transition by animateFloatAsState(
                targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f,
                label = "transition of image"
            )

            SubcomposeAsyncImageContent(
                modifier = Modifier
                    .scale(.8f + (.2f * transition))
                    .graphicsLayer { rotationX = (1f - transition) * 5f }
                    .alpha(min(1f, transition / .2f))

            )
        }

        OutlinedIconButton(
            onClick = { mediaPlayer?.start() },
            modifier = Modifier.align(Alignment.CenterStart),
            enabled = mediaPlayer != null,
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

@Composable
fun TypesContent(
    types: List<TypeUiModel>,
    onTypeClicked: (Int, String, Int?) -> Unit,
    modifier: Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        types.forEach { (id, type, color) ->
            Button(
                onClick = { onTypeClicked(id, type, color.toArgb()) },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .height(32.dp)
                    .shimmerEffect(isLoading = id == -1),
                enabled = id != -1,
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    contentColor = color.getContrastingColor()
                )
            ) {
                Text(
                    text = type.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun AboutContent(
    pokemon: PokemonDetailUiModel,
    isFullyVisible: Boolean,
    modifier: Modifier,
) {
    Text(
        text = "About",
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        pokemon.weight?.let { weight ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = "weight of the pokemon"
                    )
                    Text(
                        text = "$weight kg",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Text(
                    text = "Weight",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 8.sp,
                        lineHeight = 12.sp
                    )
                )
            }
        }
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = Dp.Hairline,
            color = MaterialTheme.colorScheme.onSurface
        ) //TODO FIX THAT
        pokemon.height?.let { height ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = "height of the pokemon",
                        modifier = Modifier.rotate(90f)
                    )
                    Text(
                        text = "$height m",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Text(
                    text = "Height",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 8.sp,
                        lineHeight = 12.sp
                    )
                )
            }
        }
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface
        ) //TODO FIX THAT
        AnimatedVisibility(visible = isFullyVisible, enter = fadeIn() + expandHorizontally()) {
            pokemon.abilities?.let { abilities ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    abilities.forEach { ability ->
                        Text(
                            text = ability.name.capitalize(Locale.ROOT),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Text(
                        text = "Moves",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 8.sp,
                            lineHeight = 12.sp
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun ColumnScope.StatisticsContentWrapper(
    stats: List<StatUiModel>,
    appDataUiState: AppDataUiState,
    color: Color,
    modifier: Modifier,
) {

    var showBarChart by remember { mutableStateOf(true) }
    val screenIsFull = LocalConfiguration.current.screenWidthDp < 600.dp.value

    Column(
        modifier = modifier.wrapContentSize(align = Alignment.CenterStart)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "Base Stats",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            if (screenIsFull) OutlinedIconButton(
                onClick = { showBarChart = !showBarChart },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                AnimatedContent(
                    targetState = showBarChart,
                    label = "Pentagon ICON",
                    transitionSpec = {
                        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left) + fadeIn(
                            animationSpec = tween(durationMillis = 500)
                        ) togetherWith
                                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right) +
                                fadeOut(animationSpec = tween(durationMillis = 500))
                    }
                ) { transition ->
                    Icon(
                        if (transition) Icons.Default.Pentagon else Icons.Default.BarChart,
                        contentDescription = null,
                    )
                }
            }
        }
        if (appDataUiState is AppDataUiState.Success) {
            if (screenIsFull)
                if ((!showBarChart)) {
                    StatisticsContentAsBarChart(
                        stats = stats,
                        globalStatList = appDataUiState.appData.globalStatList,
                        color = color,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    StatisticsContentAsRadarChart(
                        stats = stats,
                        globalStatList = appDataUiState.appData.globalStatList,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            else
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatisticsContentAsRadarChart(
                        stats = stats,
                        globalStatList = appDataUiState.appData.globalStatList,
                        modifier = Modifier
                            .padding(8.dp)
                            .widthIn(max = 300.dp)
                    )
                    StatisticsContentAsBarChart(
                        stats = stats,
                        globalStatList = appDataUiState.appData.globalStatList,
                        color = color,
                        modifier = Modifier.padding(8.dp)
                    )
                }
        }
    }
}

@Composable
fun StatisticsContentAsBarChart(
    stats: List<StatUiModel>,
    color: Color,
    globalStatList: GlobalStatList,
    modifier: Modifier,
) {
    var beginAnimation by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        beginAnimation = true
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        stats.forEachIndexed { index, stat ->
            val transition by animateFloatAsState(
                targetValue = if (beginAnimation) stat.value / 255f else 0f,
                label = "transition of the statistics ${stat.statId}"
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stat.statName.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(64.dp)
                )
                Text(
                    text = String.format("%0${3}d", stat.value),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                LinearProgressIndicator(
                    progress = { transition },
                    trackColor = Color.LightGray,
                    color = color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                color = Color.Green.copy(alpha = 0.5f),
                                topLeft = Offset(
                                    (globalStatList.statList[index].min / 255.0 * size.width).toFloat(),
                                    0f
                                ),
                                size = androidx.compose.ui.geometry.Size(
                                    10f,
                                    size.height
                                )
                            )
                            drawRect(
                                color = Color.Blue.copy(alpha = 0.5f),
                                topLeft = Offset(
                                    (globalStatList.statList[index].avg / 255.0 * size.width).toFloat(),
                                    0f
                                ),
                                size = androidx.compose.ui.geometry.Size(
                                    10f,
                                    size.height
                                )
                            )
                            drawRect(
                                color = Color.Red.copy(alpha = 0.5f),
                                topLeft = Offset(
                                    (globalStatList.statList[index].max / 255.0 * size.width).toFloat(),
                                    0f
                                ),
                                size = androidx.compose.ui.geometry.Size(
                                    10f,
                                    size.height
                                )
                            )

                        },
                    strokeCap = StrokeCap.Round
                )

            }
        }
    }
}

@Composable
fun StatisticsContentAsRadarChart(
    stats: List<StatUiModel>,
    globalStatList: GlobalStatList,
    modifier: Modifier,
) {
    var beginAnimation by rememberSaveable {
        mutableStateOf(false)
    }

    val transition = stats.map { stat ->
        animateFloatAsState(
            targetValue = if (beginAnimation) stat.value.toFloat() else 0f,
            label = "transition of the statistics ${stat.statId}"
        )
    }

    LaunchedEffect(Unit) {
        beginAnimation = true
    }

    val minPolygon = remember {
        Polygon(
            values = globalStatList.statList.map { it.min.toDouble() },
            unit = "",
            style = PolygonStyle(
                fillColor = Color.Transparent,
                fillColorAlpha = 0f,
                borderColor = Color.Green,
                borderColorAlpha = 0.9f,
                borderStrokeWidth = 1f,
                borderStrokeCap = StrokeCap.Butt
            )
        )
    }
    val avgPolygon = remember {
        Polygon(
            values = globalStatList.statList.map { it.avg },
            unit = "",
            style = PolygonStyle(
                fillColor = Color.Transparent,
                fillColorAlpha = 0f,
                borderColor = Color.Blue,
                borderColorAlpha = 0.9f,
                borderStrokeWidth = 1f,
                borderStrokeCap = StrokeCap.Butt
            )
        )
    }
    val maxPolygon = remember {
        Polygon(
            values = globalStatList.statList.map {
                Log.e("max", it.max.toString())
                it.max.toDouble()
            },
            unit = "",
            style = PolygonStyle(
                fillColor = Color.Transparent,
                fillColorAlpha = 0f,
                borderColor = Color.Red,
                borderColorAlpha = 0.9f,
                borderStrokeWidth = 1f,
                borderStrokeCap = StrokeCap.Butt
            )
        )
    }
    RadarChart(
        radarLabels = stats.mapIndexed { index, it ->
            it.statName.capitalize(Locale.ROOT) + "\n" + String.format(
                "%0${3}d",
                transition[index].value.toInt()
            )
        },
        labelsStyle = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        netLinesStyle = NetLinesStyle(
            netLineColor = MaterialTheme.colorScheme.tertiary,
            netLinesStrokeWidth = 2f,
            netLinesStrokeCap = StrokeCap.Butt
        ),
        scalarSteps = 2,
        scalarValue = 255.0,
        scalarValuesStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Transparent),
        polygons = listOf(
            minPolygon,
            avgPolygon,
            maxPolygon,
            Polygon(
                values = transition.map { it.value.toDouble() },
                style = PolygonStyle(
                    fillColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    fillColorAlpha = 0.5f,
                    borderColor = MaterialTheme.colorScheme.surfaceTint,
                    borderColorAlpha = 0.7f,
                    borderStrokeWidth = 2f,
                    borderStrokeCap = StrokeCap.Butt
                )
            )
        ),
        modifier = modifier
            .fillMaxSize()
            .offset(y = (-16).dp)
    )
}

@Composable
fun EvolutionChainContent(
    speciesUiState: PokemonSpeciesUiState,
    navigateToPokemonDetail: (Int, Int?) -> Unit,
    modifier: Modifier
) {

    when (speciesUiState) {
        PokemonSpeciesUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
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
                    modifier = modifier
                        .heightIn(min = 150.dp)
                        .fillMaxWidth()
                )
            }
        }

        else -> {}
    }
//    }
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

    val dominantColor by remember { mutableIntStateOf(pokemon.color.color.toArgb()) }

    Card(
        onClick = { onCardClick(pokemon.color.ordinal) },
        modifier = Modifier
            .padding(8.dp)
            .width(80.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(dominantColor),
            contentColor = Color(dominantColor).getContrastingColor()
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
//                .listener { _, result ->
//                    result.drawable.let {
//                        dominantColor =
//                            Palette.from(it.toBitmap()).generate().dominantSwatch?.rgb
//                                ?: surface.toArgb()
//                    }
//                }
                .build()

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
            )
        }
    }
}