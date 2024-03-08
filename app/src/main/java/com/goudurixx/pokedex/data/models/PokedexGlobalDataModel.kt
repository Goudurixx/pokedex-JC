package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.common.models.GlobalStatList
import com.goudurixx.pokedex.core.common.models.toGlobalStatList
import com.goudurixx.pokedex.core.database.models.PokedexGlobalDataDaoModel
import com.goudurixx.pokedex.core.network.models.PokedexGlobalDataReponse
import java.util.Date

data class PokedexGlobalDataModel(
    val totalPokemonCount : Int,
    val lastUpdated : Long,
    val globalStatList: GlobalStatList,
    val maxHeight: Int,
    val minHeight: Int,
    val maxWeight: Int,
    val minWeight: Int,
    val maxId: Int,
    val minBaseExperience: Int,
    val maxBaseExperience: Int
)

fun PokedexGlobalDataDaoModel.toDataModel() = PokedexGlobalDataModel(
    totalPokemonCount = totalPokemonCount,
    lastUpdated = lastUpdated,
    globalStatList = globalStatList.toGlobalStatList(),
    maxHeight = maxHeight,
    minHeight = minHeight,
    maxWeight = maxWeight,
    minWeight = minWeight,
    maxId = maxId,
    minBaseExperience = minBaseExperience,
    maxBaseExperience = maxBaseExperience
)

fun PokedexGlobalDataReponse.toDataModel() = PokedexGlobalDataModel(
    totalPokemonCount = totalPokemonCount,
    lastUpdated = Date().time,
    globalStatList = globalStatList,
    maxHeight = maxHeight,
    minHeight = minHeight,
    maxWeight = maxWeight,
    minWeight = minWeight,
    maxId = maxId,
    minBaseExperience = minBaseExperience,
    maxBaseExperience = maxBaseExperience
)