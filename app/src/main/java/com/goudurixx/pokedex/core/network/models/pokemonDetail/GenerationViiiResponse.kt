package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class GenerationViiiResponse(
    val icons: IconsResponse
)