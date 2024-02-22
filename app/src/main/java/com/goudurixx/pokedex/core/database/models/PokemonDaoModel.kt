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
    @ColumnInfo val imageUrl: String
)

fun PokemonListItemModel.toDaoModel(index: Int) = PokemonDaoModel(
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl,
    index = index
)
