package com.goudurixx.pokedex.core.common.models

import com.google.gson.Gson


data class GlobalStatList(
    val statList: List<PokemonStat>
)

data class PokemonStat(
    val id: Int,
    val name: String,
    val min: Int,
    val avg: Double,
    val max: Int
)

fun GlobalStatList.toJson(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun String.toGlobalStatList(): GlobalStatList {
    val gson = Gson()
    return gson.fromJson(this, GlobalStatList::class.java)
}