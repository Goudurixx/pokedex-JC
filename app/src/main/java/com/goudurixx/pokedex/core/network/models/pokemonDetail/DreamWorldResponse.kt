package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class DreamWorldResponse(
    val front_default: String? = null,
    val front_female: String? = null
)