package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable


@Serializable
data class PastTypeResponse(
    val generation: GenerationResponse,
    val types: List<TypeResponse>
)