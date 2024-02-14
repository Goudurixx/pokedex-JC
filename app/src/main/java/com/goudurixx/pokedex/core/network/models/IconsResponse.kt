package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class IconsResponse(
    val front_default: String? = null,
    val front_female: String? = null
)