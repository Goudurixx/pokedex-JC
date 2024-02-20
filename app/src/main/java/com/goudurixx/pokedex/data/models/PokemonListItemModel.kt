package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.PokemonListItemResponse
import com.goudurixx.pokedex.core.utils.trimIntFromUrl

data class PokemonListItemModel(
    val id : Int,
    val name: String,
    val url: String? = null,
    val imageUrl: String,
)

fun PokemonListItemResponse.toDataModel() : PokemonListItemModel{
    val id : Int = id ?: this.url?.trimIntFromUrl() ?: 0
    return PokemonListItemModel(
        id = id,
        name = name,
        url = url,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
//        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
    )
}