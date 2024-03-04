package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class VersionGroupResponse(
    val name: String,
    val url: String
)