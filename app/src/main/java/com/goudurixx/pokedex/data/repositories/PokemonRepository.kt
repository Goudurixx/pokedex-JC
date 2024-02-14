package com.goudurixx.pokedex.data.repositories

import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.data.datasources.PokemonRemoteDataSource
import com.goudurixx.pokedex.data.models.PokemonModel
import com.goudurixx.pokedex.data.models.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource
) : IPokemonRepository {
    override fun getPokemonList(limit: Int, offset: Int) {
        TODO()
//        pokemonRemoteDataSource.getPokemonList(limit, offset)
    }

    override fun getPokemonDetail(pokemonId: Int): Flow<PokemonModel> = flow {
        emit(pokemonRemoteDataSource.getPokemonDetail(pokemonId).toDataModel())
    }
}