package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class SpeciesResponse(
    val name: String,
    val url: String
)