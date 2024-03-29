package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class StatResponse(
    val base_stat: Int,
    val effort: Int,
    val stat: StatResponseX
)