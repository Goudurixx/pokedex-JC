package com.goudurixx.pokedex.data.datasources

import com.goudurixx.pokedex.core.network.IPokemonApi
import javax.inject.Inject

class PokemonRemoteDataSource @Inject constructor(
    private val pokemonApi: IPokemonApi
) {

    suspend fun getPokemonList(limit: Int, offset: Int) = pokemonApi.getPokemonList(limit, offset)

    suspend fun getPokemonDetail(id: Int) = pokemonApi.getPokemonDetail(id)
}