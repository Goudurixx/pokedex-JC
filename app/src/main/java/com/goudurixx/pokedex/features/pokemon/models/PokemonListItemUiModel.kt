package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.core.ui.theme.PokemonColor
import com.goudurixx.pokedex.data.models.PokemonListItemModel

data class PokemonListItemUiModel(
    val index: Int,
    val id: Int,
    val name: String,
    val url: String?,
    val imageUrl: String,
    val height: Int? = null,
    val weight: Int? = null,
    val baseExperience: Int? = null,
    val averageStat: Double? = null,
    val color: PokemonColor
)

fun PokemonListItemModel.toUiModel(indexIn: Int? = null) = PokemonListItemUiModel(
    index = indexIn ?: index,
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl,
    height = height,
    weight = weight,
    baseExperience = baseExperience,
    averageStat = averageStat,
    color = colorId?.let { PokemonColor.entries.getOrNull(it) } ?: PokemonColor.UNKNOWN
)

fun PokemonDaoModel.toUiModel() = PokemonListItemUiModel(
    index = index,
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl,
    height = height,
    weight = weight,
    baseExperience = baseExperience,
    averageStat = averageStat,
    color = colorId?.let { PokemonColor.entries.getOrNull(it) } ?: PokemonColor.UNKNOWN
)