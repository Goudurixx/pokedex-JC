package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class IconsResponse(
    val front_default: String? = null,
    val front_female: String? = null
)