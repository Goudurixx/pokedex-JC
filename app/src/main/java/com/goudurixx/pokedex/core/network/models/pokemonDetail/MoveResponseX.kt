package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class MoveResponseX(
    val name: String,
    val url: String
)