package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.PokemonModel

data class PokemonDetailUiModel(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String?,
//    val types: List<String>, TODO
)

fun PokemonModel.toUiModel() = PokemonDetailUiModel(
    id = id,
    name = name,
    height = height,
    weight = weight,
    imageUrl = sprites.frontDefault,
)