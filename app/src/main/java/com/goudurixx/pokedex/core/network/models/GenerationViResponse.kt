package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationViResponse(
    @SerialName("omegaruby-alphasapphire") val omegaruby_alphasapphire: OmegarubyAlphasapphireResponse,
    @SerialName("x-y") val x_y: XYResponse
)