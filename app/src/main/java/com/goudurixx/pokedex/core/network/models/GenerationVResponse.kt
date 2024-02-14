package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationVResponse(
    @SerialName("black-white") val black_white: BlackWhiteResponse
)