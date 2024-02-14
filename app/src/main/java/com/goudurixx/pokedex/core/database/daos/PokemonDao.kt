package com.goudurixx.pokedex.core.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    fun getAllPokemons(): Flow<List<PokemonDaoModel>>
}