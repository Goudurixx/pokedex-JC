package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class PastTypeResponse(
    val generation: GenerationResponse,
    val types: List<TypeResponse>
)