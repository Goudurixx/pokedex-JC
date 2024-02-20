package com.goudurixx.pokedex.core.network.models

import android.util.Log
import com.goudurixx.pokedex.PokemonGetPagedListQuery
import com.goudurixx.pokedex.PokemonSearchCompletionQuery
import com.goudurixx.pokedex.data.models.PokemonListItemModel
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListItemResponse(
    val name: String,
    val url: String? = null,
    val id: Int? = null
)

fun PokemonGetPagedListQuery.Pokemon_v2_pokemon.toResponseModel() = PokemonListItemResponse(
    id = id,
    name = name,
)

fun PokemonSearchCompletionQuery.Pokemon_v2_pokemon.toResponseModel() : PokemonListItemResponse {
    Log.e("PokemonListItemResponse", "PokemonSearchCompletionQuery.Pokemon_v2_pokemon.toResponseModel() called with : $this")
    return PokemonListItemResponse(
        id = id,
        name = name,
    )
}