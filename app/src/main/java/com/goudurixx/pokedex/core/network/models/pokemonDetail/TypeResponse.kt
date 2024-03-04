package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.Serializable

@Serializable
data class TypeResponse(
    val slot: Int,
    val type: TypeResponseX
)