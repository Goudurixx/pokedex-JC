package com.goudurixx.pokedex.core.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goudurixx.pokedex.core.database.models.PokedexGlobalDataDaoModel

@Dao
interface PokedexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokedexGlobalData: PokedexGlobalDataDaoModel)

    @Query("SELECT * FROM pokedex_global_data")
    suspend fun get(): PokedexGlobalDataDaoModel
    @Delete
    suspend fun delete(pokedexGlobalData: PokedexGlobalDataDaoModel)

}