package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.PokemonModel

data class PokemonDetailUiModel(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String?,
    val sprites: SpritesUiModel,
    val cries: String,
    val types: List<TypeUiModel>,
    val stats: List<StatUiModel>
)

fun PokemonModel.toUiModel() = PokemonDetailUiModel(
    id = id,
    name = name,
    height = height,
    weight = weight,
    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png",
    cries = cries.latest,
    sprites = sprites.toUiModel(),
    types = types.map { it.toUiModel() },
    stats = stats.map { it.toUiModel() }
)