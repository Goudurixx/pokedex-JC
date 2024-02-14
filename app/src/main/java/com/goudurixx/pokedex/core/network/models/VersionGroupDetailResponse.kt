package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class VersionGroupDetailResponse(
    val level_learned_at: Int,
    val move_learn_method: MoveLearnMethodResponse,
    val version_group: VersionGroupResponse
)