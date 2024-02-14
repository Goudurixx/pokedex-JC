package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class GameIndiceResponse(
    val game_index: Int,
    val version: VersionResponse
)