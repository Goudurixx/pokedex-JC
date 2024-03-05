package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.SpritesModel

data class SpritesUiModel(
    val backDefault: String? = null,
    val backFemale: String? = null,
    val backShiny: String? = null,
    val backShinyFemale: String? = null,
    val frontDefault: String? = null,
    val frontFemale: String? = null,
    val frontShiny: String? = null,
    val frontShinyFemale: String? = null
)

fun SpritesModel.toUiModel() = SpritesUiModel(
    backDefault = backDefault,
    backFemale = backFemale,
    backShiny = backShiny,
    backShinyFemale = backShinyFemale,
    frontDefault = frontDefault,
    frontFemale = frontFemale,
    frontShiny = frontShiny,
    frontShinyFemale = frontShinyFemale
)