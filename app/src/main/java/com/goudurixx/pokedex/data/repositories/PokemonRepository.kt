package com.goudurixx.pokedex.data.repositories

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.OrderBy
import com.goudurixx.pokedex.core.database.PokedexDatabase
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.data.datasources.PokemonLocalDataSource
import com.goudurixx.pokedex.data.datasources.PokemonRemoteDataSource
import com.goudurixx.pokedex.data.mediator.PokedexGlobalDataRemoteMediator
import com.goudurixx.pokedex.data.mediator.PokemonRemoteMediator
import com.goudurixx.pokedex.data.models.EvolutionChainModel
import com.goudurixx.pokedex.data.models.PokedexGlobalDataModel
import com.goudurixx.pokedex.data.models.PokemonListItemModel
import com.goudurixx.pokedex.data.models.PokemonModel
import com.goudurixx.pokedex.data.models.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val remoteDataSource: PokemonRemoteDataSource,
    private val localDataSource: PokemonLocalDataSource,
    private val pokedexDatabase: PokedexDatabase,
) : IPokemonRepository {

    private var orderBy: OrderBy? = null
    private var filterBy: List<FilterBy>? = null
    private var pager: Pager<Int, PokemonDaoModel>? = null

    override val appData: Flow<PokedexGlobalDataModel> =
        PokedexGlobalDataRemoteMediator(remoteDataSource, pokedexDatabase).load()

    override fun getPokemonPagerList(
        orderBy: OrderBy?,
        filterBy: List<FilterBy>?
    ): Flow<PagingData<PokemonListItemModel>> {
        return updatePaginationParams(orderBy, filterBy).flow.map { pagingData ->
            pagingData.map { it.toDataModel() }
        }
    }

    override fun getPokemonCompletion(query: String): Flow<List<PokemonListItemModel>> = flow {
        emit(remoteDataSource.getPokemonList(query = query).toDataModel().results)
    }

    override fun getPokemonDetail(id: Int): Flow<PokemonModel> = flow {
        emit(remoteDataSource.getPokemonDetail(id).toDataModel())
    }

    override fun getPokemonEvolutionChain(id: Int): Flow<EvolutionChainModel> = flow {
        emit(remoteDataSource.getPokemonEvolutionChain(id).toDataModel())
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun createPager(): Pager<Int, PokemonDaoModel> {
        val pagingKey = orderBy.toString() + filterBy.toString()
        Log.e("PokemonRepository", "createPager with pagingKey: $pagingKey")
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                prefetchDistance = 0,
                initialLoadSize = 49
            ),
            remoteMediator = PokemonRemoteMediator(
                orderBy = orderBy,
                filterBy = filterBy,
                pagingKey= pagingKey,
                remoteDataSource = remoteDataSource,
                pokedexDatabase = pokedexDatabase
            ),
            pagingSourceFactory = { localDataSource.loadAllPokemonsPaged(pagingKey) }
        )
    }

    private fun updatePaginationParams(
        orderBy: OrderBy?,
        filterBy: List<FilterBy>?
    ): Pager<Int, PokemonDaoModel> {
        Log.e(
            "PokemonRepository",
            "updatePaginationParams with orderBy: $orderBy and filterBy: $filterBy"
        )
        return if (this.filterBy != filterBy || this.orderBy != orderBy || pager == null) {
            Log.e("PokemonRepository", "updatePaginationParams with new params : \n" +
                    "orderBy:  ${this.orderBy} VS $orderBy \n" +
                    "filterBy:  ${this.filterBy} VS $filterBy \n" +
                    "pager: $pager \n")
            if (this.filterBy != filterBy) this.filterBy = filterBy
            if (this.orderBy != orderBy) this.orderBy = orderBy
            createPager()
        } else {
            Log.e("PokemonRepository", "updatePaginationParams with same params")
            pager!!
        }
    }
}