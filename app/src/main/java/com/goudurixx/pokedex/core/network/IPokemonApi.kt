package com.goudurixx.pokedex.core.network

import com.goudurixx.pokedex.core.network.models.EvolutionChainResponse
import com.goudurixx.pokedex.core.network.models.OrderByParametersNetworkModel
import com.goudurixx.pokedex.core.network.models.PokemonListResponse
import com.goudurixx.pokedex.core.network.models.PokemonResponse

interface IPokemonApi {
    suspend fun getPokemonList(limit: Int, offset: Int, orderByParameters : OrderByParametersNetworkModel?): PokemonListResponse

    suspend fun getPokemonSearchCompletion(query: String): PokemonListResponse
    suspend fun getPokemonDetail(id: Int): PokemonResponse
    suspend fun getPokemonEvolutionChain(id: Int): EvolutionChainResponse
}