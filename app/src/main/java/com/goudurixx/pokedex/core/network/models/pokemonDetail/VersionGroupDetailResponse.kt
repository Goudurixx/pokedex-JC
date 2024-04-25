package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class VersionGroupDetailResponse(
    val level_learned_at: Int,
    val move_learn_method: MoveLearnMethodResponse,
    val version_group: VersionGroupResponse
)