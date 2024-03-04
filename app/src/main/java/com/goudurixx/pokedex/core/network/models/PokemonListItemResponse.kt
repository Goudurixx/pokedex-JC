package com.goudurixx.pokedex.core.network.models

import android.util.Log
import com.goudurixx.pokedex.PokemonGetPagedListQuery
import com.goudurixx.pokedex.PokemonSearchCompletionQuery
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListItemResponse(
    val id: Int? = null,
    val name: String,
    val url: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val baseExperience: Int? = null,
    val averageStat : Double? = null
)

fun PokemonGetPagedListQuery.Pokemon_v2_pokemon.toResponseModel() = PokemonListItemResponse(
    id = id,
    name = name,
    height = height,
    weight = weight,
    baseExperience = base_experience,
    averageStat = pokemon_v2_pokemonstats_aggregate.aggregate?.avg?.base_stat
)

fun PokemonSearchCompletionQuery.Pokemon_v2_pokemon.toResponseModel() : PokemonListItemResponse {
    Log.e("PokemonListItemResponse", "PokemonSearchCompletionQuery.Pokemon_v2_pokemon.toResponseModel() called with : $this")
    return PokemonListItemResponse(
        id = id,
        name = name,
    )
}