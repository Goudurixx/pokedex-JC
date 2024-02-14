package com.goudurixx.pokedex.data

import com.goudurixx.pokedex.data.models.PokemonListModel
import com.goudurixx.pokedex.data.models.PokemonModel
import kotlinx.coroutines.flow.Flow

interface IPokemonRepository {
    fun getPokemonList(limit: Int, offset: Int) : Flow<PokemonListModel>
    fun getPokemonDetail(id: Int) : Flow<PokemonModel>
}