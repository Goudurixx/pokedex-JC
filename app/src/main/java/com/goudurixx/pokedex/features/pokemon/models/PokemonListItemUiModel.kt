package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.data.models.PokemonListItemModel

data class PokemonListItemUiModel(
    val index: Int,
    val id: Int,
    val name: String,
    val url: String?,
    val imageUrl: String
)

fun PokemonListItemModel.toUiModel(indexIn: Int? = null) = PokemonListItemUiModel(
    index = indexIn ?: index,
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl
)

fun PokemonDaoModel.toUiModel() = PokemonListItemUiModel(
    index = index,
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl
)