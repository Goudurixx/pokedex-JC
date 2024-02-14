package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class TypeResponseX(
    val name: String,
    val url: String
)