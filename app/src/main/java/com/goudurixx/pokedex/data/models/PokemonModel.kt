package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.PokemonResponse

data class PokemonModel(
    val name : String,
    val id : Int,
)

fun PokemonResponse.toDataModel() = PokemonModel(
    name = name,
    id = id
)