package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.PokemonResponse

data class PokemonModel(
    val id : Int,
    val name : String,
    val height : Int,
    val weight : Int,
    val sprites : SpritesModel,
    val cries : CriesModel,
    val abilities : List<AbilityModel>,
    val types : List<TypeModel>,
    val stats : List<StatModel>,
    val evolutionChainId : Int? = null
)

fun PokemonResponse.toDataModel() = PokemonModel(
    id = id,
    name = name,
    height = height,
    weight = weight,
    sprites = sprites.toDataModel(),
    cries = cries.toDataModel(),
    abilities = abilities.map { it.toDataModel() },
    types = types.map { it.toDataModel() },
    stats = stats.map { it.toDataModel() },
)