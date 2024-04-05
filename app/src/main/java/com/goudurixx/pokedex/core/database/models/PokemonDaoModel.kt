package com.goudurixx.pokedex.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goudurixx.pokedex.core.network.models.PokemonListItemResponse
import com.goudurixx.pokedex.core.utils.toInt
import com.goudurixx.pokedex.core.utils.trimIntFromUrl
import com.goudurixx.pokedex.data.models.PokemonListItemModel

@Entity(tableName = "pokemon")
data class PokemonDaoModel(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo val index: Int,
    @ColumnInfo(name = "paging_key") val key: String,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val url: String?,
    @ColumnInfo val imageUrl: String,
    @ColumnInfo val height: Int? = null,
    @ColumnInfo val weight: Int? = null,
    @ColumnInfo val baseExperience: Int? = null,
    @ColumnInfo val averageStat: Double? = null,
    @ColumnInfo val colorId: Int? = null,
    @ColumnInfo val generationId: Int? = null,
    @ColumnInfo val generationName: String? = null,
    @ColumnInfo val isFavorite: Int
)

fun PokemonListItemModel.toDaoModel(index: Int, key: String, isFavorite: Boolean) = PokemonDaoModel(
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl,
    height = height,
    weight = weight,
    baseExperience = baseExperience,
    averageStat = averageStat,
    colorId = colorId,
    generationId = generationId,
    generationName = generationName,
    index = index,
    key = key,
    lastUpdated = System.currentTimeMillis(),
    isFavorite = isFavorite.toInt()
)

fun PokemonListItemResponse.toDaoModel(index: Int, key: String, isFavorite: Boolean) : PokemonDaoModel {
    val id: Int = id ?: this.url?.trimIntFromUrl() ?: 0
    return PokemonDaoModel(
        id = id,
        index = index,
        name = name,
        url = url,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png",
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        averageStat = averageStat,
        colorId = colorId,
        generationId = generationId,
        generationName = generationName,
        key = key,
        lastUpdated = System.currentTimeMillis(),
        isFavorite = isFavorite.toInt()
    )
}