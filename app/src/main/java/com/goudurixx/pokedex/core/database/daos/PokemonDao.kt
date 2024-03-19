package com.goudurixx.pokedex.core.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel

@Dao
interface PokemonDao {

    @Upsert
    suspend fun upsertAll(pokemons: List<PokemonDaoModel>)

    @Query("SELECT * FROM pokemon WHERE paging_key = :key")
    fun pagingSource(key: String): PagingSource<Int, PokemonDaoModel>

    @Query("DELETE FROM pokemon WHERE paging_key = :key AND last_updated < :currentTime")
    suspend fun clearAll(key: String, currentTime: Long)
}