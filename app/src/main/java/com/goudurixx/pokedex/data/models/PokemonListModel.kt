package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.PokemonListResponse

data class PokemonListModel(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemModel>
)

fun PokemonListResponse.toDataModel() = PokemonListModel(
    count = count,
    next = next,
    previous = previous,
    results = results.map { it.toDataModel() }
)