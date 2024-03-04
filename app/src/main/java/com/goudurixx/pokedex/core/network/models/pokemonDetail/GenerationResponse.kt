package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class GenerationResponse(
    val name: String,
    val url: String
)
