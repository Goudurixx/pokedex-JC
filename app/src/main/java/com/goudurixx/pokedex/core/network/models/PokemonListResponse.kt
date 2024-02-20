package com.goudurixx.pokedex.core.network.models

import com.goudurixx.pokedex.PokemonGetPagedListQuery
import com.goudurixx.pokedex.PokemonSearchCompletionQuery
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponse(
    val count: Int? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonListItemResponse>
)

fun PokemonGetPagedListQuery.Data.toResponseModel() = PokemonListResponse(
    results = pokemon_v2_pokemon.map { it.toResponseModel() }
)

fun PokemonSearchCompletionQuery.Data.toResponseModel() = PokemonListResponse(
    results = pokemon_v2_pokemon.map { it.toResponseModel() }
)