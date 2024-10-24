package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GameIndiceResponse(
    val game_index: Int,
    val version: VersionResponse
)