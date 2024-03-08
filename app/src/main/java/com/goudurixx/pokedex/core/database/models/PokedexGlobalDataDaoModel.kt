package com.goudurixx.pokedex.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goudurixx.pokedex.core.common.models.GlobalStatList
import com.goudurixx.pokedex.core.common.models.toJson
import com.goudurixx.pokedex.data.models.PokedexGlobalDataModel
import kotlinx.serialization.json.Json


@Entity(tableName = "pokedex_global_data")
data class PokedexGlobalDataDaoModel(
    @PrimaryKey(autoGenerate = false) val totalPokemonCount: Int, // will hold the total count of pokemon
    @ColumnInfo val lastUpdated: Long,
    @ColumnInfo val globalStatList: String,
    @ColumnInfo val maxHeight: Int,
    @ColumnInfo val minHeight: Int,
    @ColumnInfo val maxWeight: Int,
    @ColumnInfo val minWeight: Int,
    @ColumnInfo val maxId: Int,
    @ColumnInfo val minBaseExperience: Int,
    @ColumnInfo val maxBaseExperience: Int

)
fun PokedexGlobalDataModel.toDaoModel() = PokedexGlobalDataDaoModel(
    totalPokemonCount = totalPokemonCount,
    lastUpdated = lastUpdated,
    globalStatList = globalStatList.toJson(),
    maxHeight = maxHeight,
    minHeight = minHeight,
    maxWeight = maxWeight,
    minWeight = minWeight,
    maxId = maxId,
    minBaseExperience = minBaseExperience,
    maxBaseExperience = maxBaseExperience
)