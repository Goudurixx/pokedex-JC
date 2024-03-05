package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.pokemonDetail.CriesResponse

data class CriesModel(
    val latest: String,
    val legacy: String? = null
)

fun CriesResponse.toDataModel() = CriesModel(
    latest = latest,
    legacy = legacy,
)