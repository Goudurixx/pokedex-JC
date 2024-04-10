package com.goudurixx.pokedex.data.datasources

import android.util.Log
import androidx.paging.PagingSource
import com.goudurixx.pokedex.core.database.daos.PokemonDao
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.core.utils.toInt
import javax.inject.Inject

class PokemonLocalDataSource @Inject constructor(
    private val pokemonDao: PokemonDao
) {
    fun loadAllPokemonsPaged(key: String): PagingSource<Int, PokemonDaoModel> =
        pokemonDao.pagingSource(key)

    suspend fun updateFavorite(pokemonId: Int, isFavorite: Boolean) {
        pokemonDao.updateFavorite(pokemonId, isFavorite.toInt())
    }

    fun getAllFavoritePokemon() = pokemonDao.getAllFavoritePokemon()

    fun getPokemonDetail(pokemonId: Int) = pokemonDao.getPokemonDetail(pokemonId)
}