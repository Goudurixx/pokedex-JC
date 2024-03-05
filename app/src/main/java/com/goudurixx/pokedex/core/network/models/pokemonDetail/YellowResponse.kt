package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class YellowResponse(
    val back_default: String? = null,
    val back_gray: String? = null,
    val back_transparent: String? = null,
    val front_default: String? = null,
    val front_gray: String? = null,
    val front_transparent: String? = null
)