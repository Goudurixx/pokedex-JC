package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class AbilityResponse(
    val ability: AbilityResponseX,
    val is_hidden: Boolean,
    val slot: Int
)