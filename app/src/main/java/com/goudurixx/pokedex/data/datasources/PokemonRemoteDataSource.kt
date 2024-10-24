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

    suspend fun getPokedexGlobalData() = pokemonApi.getPokedexGlobalData()
    suspend fun getPokemonList( limit: Int?  = null, offset: Int? = null, query: String? = null, orderBy: OrderBy? = null, filterBy: List<FilterBy>? = null) = pokemonApi.getPokemonList(limit, offset, query, orderBy?.toOrderByNetworkModel(), filterBy?.toWhereParametersNetworkModel())

    suspend fun getPokemonDetail(id: Int) = pokemonApi.getPokemonDetail(id)
    suspend fun getPokemonEvolutionChain(id: Int) = pokemonApi.getPokemonEvolutionChain(id)
}