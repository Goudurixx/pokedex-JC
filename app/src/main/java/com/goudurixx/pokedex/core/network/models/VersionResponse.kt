package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class VersionResponse(
    val name: String,
    val url: String
)