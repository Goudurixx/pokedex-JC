package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GenerationViResponse(
    @SerialName("omegaruby-alphasapphire") val omegaruby_alphasapphire: OmegarubyAlphasapphireResponse,
    @SerialName("x-y") val x_y: XYResponse
)