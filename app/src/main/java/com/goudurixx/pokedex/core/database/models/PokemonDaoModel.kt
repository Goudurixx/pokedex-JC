package com.goudurixx.pokedex.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonDaoModel(
    @field:PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val image: String,
)
