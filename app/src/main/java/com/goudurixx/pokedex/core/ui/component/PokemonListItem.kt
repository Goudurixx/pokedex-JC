package com.goudurixx.pokedex.core.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.common.models.OrderByParameter
import com.goudurixx.pokedex.core.ui.theme.PokedexTheme
import com.goudurixx.pokedex.core.ui.theme.PokemonColor
import com.goudurixx.pokedex.core.ui.utils.shimmerEffect
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun PokemonListItem(
    pokemon: PokemonListItemUiModel,
    enabled: Boolean,
    backgroundColor: Int,
    selectedFilter: SortOrderItem?,
    onItemClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 100.dp,
    onUpdateFavorite: (Int, Boolean) -> Unit
) {
    val dominantColor by remember(pokemon) { mutableIntStateOf(pokemon.color.color.toArgb()) }

    Card(
        onClick = { onItemClick(pokemon.id, pokemon.color.ordinal) },
        modifier = modifier
            .heightIn(max = itemHeight)
            .fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(2.dp, Color(dominantColor).copy(alpha = 0.5f))
    ) {
        var atEnd by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(itemHeight)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(dominantColor).copy(alpha = 0.5f))
                    .border(
                        BorderStroke(2.dp, Color(dominantColor).copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(8.dp)
            ) {
                SubcomposeAsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = null
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                        LaunchedEffect(Unit) {
                            atEnd = !atEnd
                        }
                        val loadingImage =
                            AnimatedImageVector.animatedVectorResource(R.drawable.pokeball_loader)
                        Image(
                            rememberAnimatedVectorPainter(loadingImage, atEnd),
                            null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .alpha(0.3f)
                                .fillMaxSize()

                        )
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
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

            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .fillMaxHeight()
            ) {
                pokemon.generationName?.let { generationName ->
                    val shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 0.dp
                    )
                    Box(
                        modifier = Modifier
                            .clip(shape)
                            .background(Color(dominantColor).copy(alpha = 0.5f))
                            .border(
                                BorderStroke(2.dp, Color(dominantColor).copy(alpha = 0.3f)),
                                shape = shape
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = generationName.toUpperCase(Locale.ROOT)
                                .removeRange("GEN".length, "GENERATION".length)
                        )
                    }
                }
                IconButton(onClick = {
                    onUpdateFavorite(pokemon.id, !pokemon.isFavorite)
                }) {
                    AnimatedContent(targetState = pokemon.isFavorite) { targetFavorite ->
                        if (targetFavorite) {
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

                    }
                }
            }
        }
    }
}

@Composable
fun PokemonListItemLoading(itemHeight: Dp = 100.dp) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(itemHeight)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    .border(
                        BorderStroke(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .shimmerEffect()
            ) {}
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .padding(vertical = 4.dp)
                        .height(15.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
            val shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 20.dp,
                bottomStart = 20.dp,
                bottomEnd = 0.dp
            )
            Column(Modifier.align(Alignment.Top)) {
                Box(
                    modifier = Modifier
                        .clip(shape)
                        .border(
                            BorderStroke(
                                2.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            shape = shape
                        )
                        .width(64.dp)
                        .height(32.dp)
                        .shimmerEffect()
                ) {
                }
               IconButton(onClick = { /*TODO*/ }, enabled = false) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.Transparent,
                        modifier = Modifier.clip(RoundedCornerShape(16.dp)).shimmerEffect()
                    )
                }
            }
        }
    }
}

@Preview(name = "PokemonListItem", showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun PokemonListItemPreview() {
    PokedexTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PokemonListItem(
                pokemon = PokemonListItemUiModel(
                    id = 1,
                    name = "bulbasaur",
                    url = null,
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    height = 1,
                    weight = 10,
                    baseExperience = 64,
                    averageStat = 64.0,
                    color = PokemonColor.BLUE,
                    generationName = "generation-i",
                    index = 1,
                    isFavorite = false
                ),
                enabled = true,
                backgroundColor = Color.Red.toArgb(),
                selectedFilter = null,
                onItemClick = { _, _ -> },
                onUpdateFavorite = { _, _ -> }
            )
            Spacer(modifier = Modifier.size(8.dp))
            PokemonListItem(
                pokemon = PokemonListItemUiModel(
                    id = 2,
                    name = "ivisaur",
                    url = null,
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    height = 1,
                    weight = 10,
                    baseExperience = 64,
                    averageStat = 64.0,
                    color = PokemonColor.PURPLE,
                    index = 1,
                    isFavorite = true
                ),
                enabled = true,
                backgroundColor = Color.Blue.toArgb(),
                selectedFilter = null,
                onItemClick = { _, _ -> },
                onUpdateFavorite = { _, _ -> }
            )
            Spacer(modifier = Modifier.size(8.dp))
            PokemonListItemLoading()
        }
    }
}