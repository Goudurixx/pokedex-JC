package com.goudurixx.pokedex.core.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.common.models.OrderByParameter
import com.goudurixx.pokedex.core.ui.theme.PokedexTheme
import com.goudurixx.pokedex.core.ui.theme.PokemonColor
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PokemonListItem(
    pokemon: PokemonListItemUiModel,
    enabled: Boolean,
    favorite: Boolean,
    onAddToFavorite: (Boolean) -> Unit,
    backgroundColor: Int,
    selectedFilter: SortOrderItem?,
    onItemClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dominantColor by remember { mutableIntStateOf(pokemon.color.color.toArgb()) }
    var imageLoaded by remember { mutableStateOf(false) }
    val imageAlpha by animateFloatAsState(
        targetValue = if (imageLoaded) 1f else 0.3f, label = "imageAlpha"
    )
    Card(
        onClick = { onItemClick(pokemon.id, dominantColor) },
        modifier = modifier
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

        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(pokemon.imageUrl)
            .allowHardware(false)
            .crossfade(true)
            .listener { _, _ ->
                imageLoaded = true
            }
            .placeholder(R.drawable.pokeball)
            .build()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(dominantColor).copy(alpha = 0.5f))
                    .border(
                        BorderStroke(2.dp, Color(dominantColor).copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(20.dp))
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer {
                        this.alpha = imageAlpha
                    },
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                    index = 1,
                ),
                enabled = true,
                backgroundColor = Color.Red.toArgb(),
                selectedFilter = null,
                onItemClick = { _, _ -> },
                favorite = true,
                onAddToFavorite = {}
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
                    index = 1
                ),
                enabled = true,
                backgroundColor = Color.Blue.toArgb(),
                selectedFilter = null,
                onItemClick = { _, _ -> },
                favorite = false,
                onAddToFavorite = {}
            )
        }
    }
}