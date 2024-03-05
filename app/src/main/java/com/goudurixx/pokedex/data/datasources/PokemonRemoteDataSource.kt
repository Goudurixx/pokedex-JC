package com.goudurixx.pokedex.data.datasources

import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.OrderBy
import com.goudurixx.pokedex.core.network.IPokemonApi
import com.goudurixx.pokedex.core.network.models.toOrderByNetworkModel
import com.goudurixx.pokedex.core.network.models.toWhereParametersNetworkModel
import javax.inject.Inject

class PokemonRemoteDataSource @Inject constructor(
    private val pokemonApi: IPokemonApi
) {

    suspend fun getPokemonList(limit: Int, offset: Int, orderBy: OrderBy?, filterBy: List<FilterBy>?) = pokemonApi.getPokemonList(limit, offset, orderBy?.toOrderByNetworkModel(), filterBy?.toWhereParametersNetworkModel())

    suspend fun getPokemonSearchCompletion(query: String) = pokemonApi.getPokemonSearchCompletion(query)

    suspend fun getPokemonDetail(id: Int) = pokemonApi.getPokemonDetail(id)
    suspend fun getPokemonEvolutionChain(id: Int) = pokemonApi.getPokemonEvolutionChain(id)
}