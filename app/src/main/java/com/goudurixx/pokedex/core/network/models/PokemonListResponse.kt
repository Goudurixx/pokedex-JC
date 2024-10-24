package com.goudurixx.pokedex.core.network.models

import androidx.annotation.Keep
import com.goudurixx.pokedex.PokemonGetPagedListQuery
import kotlinx.serialization.Serializable

@Keep
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