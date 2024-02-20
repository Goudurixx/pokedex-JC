package com.goudurixx.pokedex.data.repositories

import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.data.datasources.PokemonRemoteDataSource
import com.goudurixx.pokedex.data.models.PokemonModel
import com.goudurixx.pokedex.data.models.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val remoteDataSource: PokemonRemoteDataSource,
) : IPokemonRepository {
    override fun getPokemonList(limit: Int, offset: Int) = flow {
        emit(remoteDataSource.getPokemonList(limit, offset).toDataModel())
    }

    override fun getPokemonCompletion(query: String): Flow<List<PokemonListItemModel>> = flow {
        emit(remoteDataSource.getPokemonSearchCompletion(query).toDataModel().results)
    }

    override fun getPokemonDetail(id: Int): Flow<PokemonModel> = flow {
        emit(remoteDataSource.getPokemonDetail(id).toDataModel())
    }
}