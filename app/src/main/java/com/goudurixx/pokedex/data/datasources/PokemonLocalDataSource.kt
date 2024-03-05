package com.goudurixx.pokedex.data.datasources

import androidx.paging.PagingSource
import com.goudurixx.pokedex.core.database.daos.PokemonDao
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import javax.inject.Inject

class PokemonLocalDataSource @Inject constructor(
    private val pokemonDao: PokemonDao
){
    fun loadAllPokemonsPaged() : PagingSource<Int, PokemonDaoModel> = pokemonDao.pagingSource()
}