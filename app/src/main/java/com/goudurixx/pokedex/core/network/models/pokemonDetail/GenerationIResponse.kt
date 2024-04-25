package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GenerationIResponse(
    @SerialName("red-blue") val red_blue: RedBlueResponse,
    val yellow: YellowResponse
)