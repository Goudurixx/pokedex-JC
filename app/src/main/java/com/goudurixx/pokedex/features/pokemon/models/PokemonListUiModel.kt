package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.PokemonListModel

data class PokemonListUiModel(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemUiModel>
)

fun PokemonListModel.toUiModel() = PokemonListUiModel(
    count = count,
    next = next,
    previous = previous,
    results = results.mapIndexed { index, pokemon -> pokemon.toUiModel(index) }
)