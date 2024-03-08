package com.goudurixx.pokedex.core.network.models

import com.goudurixx.pokedex.GetPokedexGlobalDataQuery
import com.goudurixx.pokedex.core.common.models.GlobalStatList
import com.goudurixx.pokedex.core.common.models.PokemonStat

data class PokedexGlobalDataReponse(
    val totalPokemonCount: Int,
    val globalStatList: GlobalStatList,
    val maxHeight: Int,
    val minHeight: Int,
    val maxWeight: Int,
    val minWeight: Int,
    val maxId: Int,
    val minBaseExperience: Int,
    val maxBaseExperience: Int
)

fun GetPokedexGlobalDataQuery.Data.toResponseModel() = PokedexGlobalDataReponse(
    totalPokemonCount = pokemon_v2_pokemon_aggregate.aggregate!!.count, //TODO: check if this is the correct field
    globalStatList = GlobalStatList(pokemon_v2_pokemonstat.map {
        PokemonStat(
            id = it.pokemon_v2_stat!!.id,
            name = it.pokemon_v2_stat.name,
            min = it.pokemon_v2_stat.pokemon_v2_pokemonstats_aggregate.statFields.aggregate!!.min!!.base_stat!!,
            avg = it.pokemon_v2_stat.pokemon_v2_pokemonstats_aggregate.statFields.aggregate.avg!!.base_stat!!,
            max = it.pokemon_v2_stat.pokemon_v2_pokemonstats_aggregate.statFields.aggregate.max!!.base_stat!!
        )
    }),
    maxHeight = pokemon_v2_pokemon_aggregate.aggregate.max!!.height!!,
    minHeight = pokemon_v2_pokemon_aggregate.aggregate.min!!.height!!,
    maxWeight = pokemon_v2_pokemon_aggregate.aggregate.max.weight!!,
    minWeight = pokemon_v2_pokemon_aggregate.aggregate.min.weight!!,
    maxId = pokemon_v2_pokemon_aggregate.aggregate.max.id!!,
    minBaseExperience = pokemon_v2_pokemon_aggregate.aggregate.min.base_experience!!,
    maxBaseExperience = pokemon_v2_pokemon_aggregate.aggregate.max.base_experience!!
)