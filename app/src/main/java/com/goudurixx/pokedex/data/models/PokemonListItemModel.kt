package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.PokemonListItemResponse

data class PokemonListItemModel(
    val id : Int,
    val name: String,
    val url: String,
    val imageUrl: String,
)

fun PokemonListItemResponse.toDataModel() : PokemonListItemModel{
    val id : Int = this.url.substringBeforeLast("/").substringAfterLast("/").toInt()
    return PokemonListItemModel(
        id = id,
        name = name,
        url = url,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
    )
}