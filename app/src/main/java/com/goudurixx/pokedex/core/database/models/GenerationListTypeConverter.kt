package com.goudurixx.pokedex.core.database.models

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GenerationListTypeConverter {
    @TypeConverter
    fun fromGenerationList(value: List<Pair<Int, String>>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toGenerationList(value: String): List<Pair<Int, String>> {
        return Json.decodeFromString(value)
    }
}