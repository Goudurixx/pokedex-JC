package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RubySapphireResponse(
    val back_default: String? = null,
    val back_shiny: String? = null,
    val front_default: String? = null,
    val front_shiny: String? = null
)