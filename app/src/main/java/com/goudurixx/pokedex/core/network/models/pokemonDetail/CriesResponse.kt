package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class CriesResponse(
    val latest: String,
    val legacy: String? = null
)