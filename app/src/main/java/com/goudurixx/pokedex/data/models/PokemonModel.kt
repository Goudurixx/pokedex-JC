package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.PokemonResponse

data class PokemonModel(
    val id: Int,
    val name: String,
    val height: Int? = null,
    val weight: Int? = null,
    val sprites: SpritesModel? = null,
    val cries: CriesModel? = null,
    val abilities: List<AbilityModel>? = null,
    val types: List<TypeModel>? = null,
    val stats: List<StatModel>? = null,
    val evolutionChainId: Int? = null,
    val isFavorite: Boolean
)

fun PokemonResponse.toDataModel(isFavorite: Boolean) = PokemonModel(
    id = id,
    name = name,
    height = height,
    weight = weight,
    sprites = sprites.toDataModel(),
    cries = cries.toDataModel(),
    abilities = abilities.map { it.toDataModel() },
    types = types.map { it.toDataModel() },
    stats = stats.map { it.toDataModel() },
    isFavorite = isFavorite
)