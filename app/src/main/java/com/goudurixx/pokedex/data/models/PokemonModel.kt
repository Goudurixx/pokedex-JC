package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.PokemonResponse

data class PokemonModel(
    val id : Int,
    val name : String,
    val height : Int,
    val weight : Int,
    val sprites : SpritesModel
)

fun PokemonResponse.toDataModel() = PokemonModel(
    id = id,
    name = name,
    height = height,
    weight = weight,
    sprites = sprites.toDataModel(),
)