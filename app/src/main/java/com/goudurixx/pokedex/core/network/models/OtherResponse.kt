package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtherResponse(
    val dream_world: DreamWorldResponse,
    val home: HomeResponse,
    @SerialName("official-artwork") val official_artwork: OfficialArtworkResponse,
    val showdown: ShowdownResponse
)