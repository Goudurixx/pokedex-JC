package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class StatResponse(
    val base_stat: Int,
    val effort: Int,
    val stat: StatResponseX
)