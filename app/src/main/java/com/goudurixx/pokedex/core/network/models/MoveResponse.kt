package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class MoveResponse(
    val move: MoveResponseX,
    val version_group_details: List<VersionGroupDetailResponse>
)