package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.database.models.PokedexGlobalDataDaoModel
import com.goudurixx.pokedex.core.network.models.PokedexGlobalDataReponse
import java.util.Date

data class PokedexGlobalDataModel(
    val totalPokemonCount : Int,
    val lastUpdated : Long,
)

fun PokedexGlobalDataDaoModel.toDataModel() = PokedexGlobalDataModel(
    totalPokemonCount = totalPokemonCount,
    lastUpdated = lastUpdated
)

fun PokedexGlobalDataReponse.toDataModel() = PokedexGlobalDataModel(
    totalPokemonCount = totalPokemonCount,
    lastUpdated = Date().time
)