package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.PokemonListItemResponse

data class PokemonListItemModel(
    val name: String,
    val url: String
)

fun PokemonListItemResponse.toDataModel() = PokemonListItemModel(
    name = name,
    url = url
)