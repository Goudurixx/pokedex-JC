package com.goudurixx.pokedex.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goudurixx.pokedex.data.models.PokedexGlobalDataModel


@Entity(tableName = "pokedex_global_data")
data class PokedexGlobalDataDaoModel(
    @PrimaryKey(autoGenerate = false) val totalPokemonCount: Int, // will hold the total count of pokemon
    @ColumnInfo val lastUpdated: Long,
//    @ColumnInfo(name = "average_height") val averageHeight: Double,
//    @ColumnInfo(name = "average_weight") val averageWeight: Double,
//    @ColumnInfo(name = "pokemon_count_by_type") val pokemonCountByType: Map<String, Int>,
//    @ColumnInfo(name = "expiration_date") val expirationDate: Date,
)
fun PokedexGlobalDataModel.toDaoModel() = PokedexGlobalDataDaoModel(
    totalPokemonCount = totalPokemonCount,
    lastUpdated = lastUpdated
)