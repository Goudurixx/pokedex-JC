package com.goudurixx.pokedex.data

import androidx.paging.PagingData
import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.OrderBy
import com.goudurixx.pokedex.data.models.EvolutionChainModel
import com.goudurixx.pokedex.data.models.PokedexGlobalDataModel
import com.goudurixx.pokedex.data.models.PokemonListItemModel
import com.goudurixx.pokedex.data.models.PokemonModel
import kotlinx.coroutines.flow.Flow

interface IPokemonRepository {

    val appData: Flow<PokedexGlobalDataModel>

    fun getPokemonPagerList(orderBy: OrderBy?, filterBy : List<FilterBy>) :  Flow<PagingData<PokemonListItemModel>>
    fun getPokemonCompletion(query: String) : Flow<List<PokemonListItemModel>>
    fun getPokemonDetail(id: Int) : Flow<PokemonModel>

    fun getPokemonEvolutionChain(id: Int): Flow<EvolutionChainModel>
}