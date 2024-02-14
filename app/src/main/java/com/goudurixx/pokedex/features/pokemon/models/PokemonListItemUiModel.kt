package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.PokemonListItemModel

data class PokemonListItemUiModel(
    val id: Int,
    val name: String,
    val url: String,
    val image: String
)

fun PokemonListItemModel.toUiModel() = PokemonListItemUiModel(
    id = url.toId(),
    name = name,
    url = url,
    image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${url.toId()}.png"
)

private fun String.toId() : Int = substringBeforeLast("/").substringAfterLast("/").toInt()