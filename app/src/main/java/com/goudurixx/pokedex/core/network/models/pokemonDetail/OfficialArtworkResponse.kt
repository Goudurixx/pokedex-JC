package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class OfficialArtworkResponse(
    val front_default: String? = null,
    val front_shiny: String? = null
)