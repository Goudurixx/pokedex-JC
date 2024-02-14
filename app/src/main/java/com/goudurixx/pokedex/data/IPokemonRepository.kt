package com.goudurixx.pokedex.data

import com.goudurixx.pokedex.data.models.PokemonModel
import kotlinx.coroutines.flow.Flow

interface IPokemonRepository {
    fun getPokemonList(limit: Int, offset: Int) : Unit
    fun getPokemonDetail(pokemonId: Int) : Flow<PokemonModel>
}