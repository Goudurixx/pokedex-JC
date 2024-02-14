package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationIiiResponse(
    val emerald: EmeraldResponse,
    @SerialName("firered-leafgreen")val firered_leafgreen: FireredLeafgreenResponse,
    @SerialName("ruby-sapphire") val ruby_sapphire: RubySapphireResponse
)