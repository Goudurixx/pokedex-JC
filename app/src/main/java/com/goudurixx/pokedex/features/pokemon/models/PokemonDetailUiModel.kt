package com.goudurixx.pokedex.features.pokemon.models

import androidx.compose.ui.graphics.Color
import com.goudurixx.pokedex.core.ui.theme.PokemonColor
import com.goudurixx.pokedex.data.models.PokemonModel
import com.goudurixx.pokedex.data.models.StatModel
import java.util.Locale

data class PokemonDetailUiModel(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String?,
    val sprites: SpritesUiModel,
    val cries: String,
    val abilities: List<AbilityUiModel>,
    val types: List<TypeUiModel>,
    val stats: List<StatUiModel>,
    val color : PokemonColor
){
    companion object {
        fun placeHolder(id : Int?, color : Int) = PokemonDetailUiModel(
            id = id ?: -1,
            name = "",
            height = 0,
            weight = 0,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png",
            cries = "",
            sprites = SpritesUiModel(),
            abilities = listOf(AbilityUiModel.placeHolder()),
            types = listOf(TypeUiModel(id = -1, name = "Loading...", color = Color.Gray)),
            stats = List(6) { index ->
                StatModel(
                    statName = "",
                    statId = index + 1,
                    value = 0,
                    effort = 0
                ).toUiModel()
            },
            color = PokemonColor.entries[color]
        )
    }
}

fun PokemonModel.toUiModel(color: Int?) = PokemonDetailUiModel(
    id = id,
    name = name.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    },
    height = height,
    weight = weight,
    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png",
    cries = cries.latest,
    sprites = sprites.toUiModel(),
    abilities = abilities.map { it.toUiModel() },
    types = types.map { it.toUiModel() },
    stats = stats.map { it.toUiModel() },
    color = PokemonColor.entries[color ?: 0]
)