package com.goudurixx.pokedex.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.goudurixx.pokedex.core.database.PokedexDatabase
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.core.database.models.toDaoModel
import com.goudurixx.pokedex.core.network.IPokemonApi
import com.goudurixx.pokedex.data.models.toDataModel
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator @Inject constructor(
    private val pokemonApi: IPokemonApi,
    private val pokedexDatabase: PokedexDatabase
) : RemoteMediator<Int, PokemonDaoModel>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonDaoModel>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        0
                    } else {
                        (lastItem.id + 1 / state.config.pageSize)
                    }
                }
            }

            val response =
                pokemonApi.getPokemonList(state.config.pageSize, loadKey).toDataModel().results

            pokedexDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokedexDatabase.pokemonDao().clearAll()
                }

                val pokemonDaos = response.map { it.toDaoModel() }
                pokedexDatabase.pokemonDao().upsertAll(pokemonDaos)
            }

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpRequestTimeoutException) {
            MediatorResult.Error(e)
        }
    }
}