package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.core.network.models.PokemonListItemResponse
import com.goudurixx.pokedex.core.utils.trimIntFromUrl

data class PokemonListItemModel(
    val index: Int,
    val id: Int,
    val name: String,
    val url: String? = null,
    val imageUrl: String,
    val height : Int? = null,
    val weight : Int? = null,
    val baseExperience : Int? = null,
    val averageStat: Double? = null,
    val colorId: Int? = null
)

fun PokemonListItemResponse.toDataModel(): PokemonListItemModel {
    val id: Int = id ?: this.url?.trimIntFromUrl() ?: 0
    return PokemonListItemModel(
        index = id,
        id = id,
        name = name,
        url = url,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png",
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        averageStat = averageStat,
        colorId = colorId
    )
}

fun PokemonDaoModel.toDataModel(): PokemonListItemModel {
    return PokemonListItemModel(
        index = index,
        id = id,
        name = name,
        url = url,
        imageUrl = imageUrl,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        averageStat = averageStat,
        colorId = colorId
    )
}