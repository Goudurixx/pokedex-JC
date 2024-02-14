package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationViiResponse(
    val icons: IconsResponse,
    @SerialName("ultra-sun-ultra-moon") val ultra_sun_ultra_moon: UltraSunUltraMoonResponse
)