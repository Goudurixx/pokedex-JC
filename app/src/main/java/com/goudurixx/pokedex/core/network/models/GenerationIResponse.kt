package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationIResponse(
    @SerialName("red-blue") val red_blue: RedBlueResponse,
    val yellow: YellowResponse
)