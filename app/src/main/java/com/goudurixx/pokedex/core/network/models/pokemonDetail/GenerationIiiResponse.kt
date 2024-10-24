package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GenerationIiiResponse(
    val emerald: EmeraldResponse,
    @SerialName("firered-leafgreen")val firered_leafgreen: FireredLeafgreenResponse,
    @SerialName("ruby-sapphire") val ruby_sapphire: RubySapphireResponse
)