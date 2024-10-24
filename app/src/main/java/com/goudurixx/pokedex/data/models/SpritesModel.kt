package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.pokemonDetail.SpritesResponse

data class SpritesModel(
    val backDefault: String? = null,
    val backFemale: String? = null,
    val backShiny: String? = null,
    val backShinyFemale: String? = null,
    val frontDefault: String? = null,
    val frontFemale: String? = null,
    val frontShiny: String? = null,
    val frontShinyFemale: String? = null,
)

fun SpritesResponse.toDataModel() = SpritesModel(
    backDefault = back_default,
    backFemale = back_female,
    backShiny = back_shiny,
    backShinyFemale = back_shiny_female,
    frontDefault = front_default,
    frontFemale = front_female,
    frontShiny = front_shiny,
    frontShinyFemale = front_shiny_female
)

/**
 * This function is used to create a [SpritesModel] from a single url
 *  defaulting to the frontDefault
 *  @param url the url to use
 */
fun spritesFromUrl(url : String) = SpritesModel(frontDefault = url)