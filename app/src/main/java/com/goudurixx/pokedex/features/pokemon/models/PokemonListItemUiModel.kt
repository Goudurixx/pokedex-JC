package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.data.models.PokemonListItemModel

data class PokemonListItemUiModel(
    val id: Int,
    val name: String,
    val url: String,
    val imageUrl: String
)

fun PokemonListItemModel.toUiModel() = PokemonListItemUiModel(
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl
)

fun PokemonDaoModel.toUiModel() = PokemonListItemUiModel(
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl
)