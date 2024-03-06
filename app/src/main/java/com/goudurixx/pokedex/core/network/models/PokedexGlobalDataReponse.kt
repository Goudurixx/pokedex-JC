package com.goudurixx.pokedex.core.network.models

import com.goudurixx.pokedex.GetPokedexGlobalDataQuery

data class PokedexGlobalDataReponse(
    val totalPokemonCount: Int,
)

fun GetPokedexGlobalDataQuery.Data.toResponseModel() = PokedexGlobalDataReponse(
    totalPokemonCount = pokemon_v2_pokemon_aggregate.aggregate!!.count //TODO: check if this is the correct field
)