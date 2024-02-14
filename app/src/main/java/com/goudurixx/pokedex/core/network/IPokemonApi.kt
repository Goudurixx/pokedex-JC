package com.goudurixx.pokedex.core.network

import com.goudurixx.pokedex.core.network.models.PokemonListResponse
import com.goudurixx.pokedex.core.network.models.PokemonResponse

interface IPokemonApi {
    suspend fun getPokemonList(limit: Int, offset: Int): PokemonListResponse

    suspend fun getPokemonDetail(id: Int): PokemonResponse
}