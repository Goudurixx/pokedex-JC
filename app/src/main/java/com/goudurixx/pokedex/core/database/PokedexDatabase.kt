package com.goudurixx.pokedex.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.goudurixx.pokedex.core.database.daos.PokemonDao
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel

@Database(
    entities = [PokemonDaoModel::class],
    version = 1,
    exportSchema = false
)
abstract class PokedexDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}