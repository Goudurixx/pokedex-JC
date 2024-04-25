package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoveResponse(
    val move: MoveResponseX,
    val version_group_details: List<VersionGroupDetailResponse>
)