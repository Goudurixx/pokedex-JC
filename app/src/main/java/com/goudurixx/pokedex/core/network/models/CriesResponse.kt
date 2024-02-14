package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class CriesResponse(
    val latest: String,
    val legacy: String
)