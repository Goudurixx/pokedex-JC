package com.goudurixx.pokedex.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.OrderBy
import com.goudurixx.pokedex.core.database.PokedexDatabase
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.core.database.models.toDaoModel
import com.goudurixx.pokedex.data.datasources.PokemonRemoteDataSource
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator @Inject constructor(
    private val orderBy: OrderBy? = null,
    private val filterBy: List<FilterBy>? = null,
    private val pagingKey: String,
    private val remoteDataSource: PokemonRemoteDataSource,
    private val pokedexDatabase: PokedexDatabase
) : RemoteMediator<Int, PokemonDaoModel>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonDaoModel>,
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    Log.d("PokemonRemoteMediator", "APPEND : $pagingKey")
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        0
                    } else {
                        lastItem.index + 1 / state.config.pageSize + 1
                    }
                }
            }

            val response =
                try {
                    remoteDataSource.getPokemonList(
                        limit = state.config.pageSize,
                        offset = loadKey,
                        orderBy = orderBy,
                        filterBy = filterBy
                    ).results
                } catch (e: Exception) {
                    Log.e("PokemonRemoteMediator", "An error occured while trying to fetch list", e)
                    return MediatorResult.Error(e)
                }
            pokedexDatabase.withTransaction {
                val favoritePokemon = pokedexDatabase.pokemonDao().getAllFavoritePokemon().first()
                if (loadType == LoadType.REFRESH) {
                    pokedexDatabase.pokemonDao().clearAll(pagingKey, System.currentTimeMillis() - 1200000)
                }

                val pokemonDaos = response.mapIndexed { index, pokemon ->
                    val isFavorite = favoritePokemon.any { favoritePokemon ->
                        favoritePokemon.id == pokemon.id
                    }
                    pokemon.toDaoModel(index + loadKey, pagingKey, isFavorite)
                }
                pokedexDatabase.pokemonDao().upsertAll(pokemonDaos)
            }

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: IOException) {
            Log.e("PokemonRemoteMediator", "IOException", e)
            MediatorResult.Error(e)
        } catch (e: HttpRequestTimeoutException) {
            Log.e("PokemonRemoteMediator", "HttpRequestTimeoutException", e)
            MediatorResult.Error(e)
        }
    }
}