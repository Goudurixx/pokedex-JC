package com.goudurixx.pokedex.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goudurixx.pokedex.data.models.PokemonListItemModel

@Entity(tableName = "pokemon")
data class PokemonDaoModel(
    @ColumnInfo val id: Int,
    @PrimaryKey(autoGenerate = false) val index: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val url: String?,
    @ColumnInfo val imageUrl: String,
    @ColumnInfo val height: Int? = null,
    @ColumnInfo val weight: Int? = null,
    @ColumnInfo val baseExperience: Int? = null
)

fun PokemonListItemModel.toDaoModel(index: Int) = PokemonDaoModel(
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl,
    height = height,
    weight = weight,
    baseExperience = baseExperience,
    index = index
)
