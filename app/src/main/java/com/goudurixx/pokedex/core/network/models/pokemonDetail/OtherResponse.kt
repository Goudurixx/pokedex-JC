package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class OtherResponse(
    val dream_world: DreamWorldResponse,
    val home: HomeResponse,
    @SerialName("official-artwork") val official_artwork: OfficialArtworkResponse,
    val showdown: ShowdownResponse
)