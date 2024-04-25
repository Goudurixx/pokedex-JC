package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AbilityResponse(
    val ability: AbilityResponseX,
    val is_hidden: Boolean,
    val slot: Int
)