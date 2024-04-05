package com.goudurixx.pokedex.core.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Upsert
    suspend fun upsertAll(pokemons: List<PokemonDaoModel>)

    @Query("SELECT * FROM pokemon WHERE paging_key = :key")
    fun pagingSource(key: String): PagingSource<Int, PokemonDaoModel>

    @Query("SELECT * FROM pokemon WHERE isFavorite = 1")
    fun getAllFavoritePokemon(): Flow<List<PokemonDaoModel>>

    @Query("UPDATE pokemon SET isFavorite = :isFavorite WHERE id = :pokemonId")
    suspend fun updateFavorite(pokemonId: Int, isFavorite: Int)

    @Query("DELETE FROM pokemon WHERE paging_key = :key AND last_updated < :currentTime AND isFavorite = 0")
    suspend fun clearAll(key: String, currentTime: Long)
}