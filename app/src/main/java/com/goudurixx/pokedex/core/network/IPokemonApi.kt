package com.goudurixx.pokedex.core.network

import com.goudurixx.pokedex.core.network.models.EvolutionChainResponse
import com.goudurixx.pokedex.core.network.models.OrderByParametersNetworkModel
import com.goudurixx.pokedex.core.network.models.PokedexGlobalDataReponse
import com.goudurixx.pokedex.core.network.models.PokemonListResponse
import com.goudurixx.pokedex.core.network.models.PokemonResponse
import com.goudurixx.pokedex.core.network.models.WhereParametersNetworkModel

interface IPokemonApi {

    suspend fun getPokedexGlobalData(): PokedexGlobalDataReponse

    suspend fun getPokemonList(
        limit: Int?, offset: Int?, query: String?,
        orderByParameters: OrderByParametersNetworkModel?,
        whereParameters: WhereParametersNetworkModel?
    ): PokemonListResponse

    suspend fun getPokemonDetail(id: Int): PokemonResponse
    suspend fun getPokemonEvolutionChain(id: Int): EvolutionChainResponse
}