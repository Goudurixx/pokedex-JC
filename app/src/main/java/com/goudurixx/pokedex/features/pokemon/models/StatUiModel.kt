package com.goudurixx.pokedex.features.pokemon.models

import androidx.compose.ui.graphics.Color
import com.goudurixx.pokedex.core.ui.theme.*
import com.goudurixx.pokedex.data.models.StatModel

data class StatUiModel(
    val statName: String,
    val statId: Int,
    val value: Int,
    val effort: Int,
    val color : Color
)

fun StatModel.toUiModel() = StatUiModel(
    statName = when(statId) {
        1 -> "HP"
        2 -> "ATK"
        3 -> "DEF"
        4 -> "SATK"
        5 -> "SPDEF"
        6 -> "SPD"
        else -> "Unknown Stat"
    },
    statId = statId,
    value = value,
    effort = effort,
    color = when(statId) {
        1 -> HPColor
        2 -> AtkColor
        3 -> DefColor
        4 -> SpAtkColor
        5 -> SpDefColor
        6 -> SpdColor
        else -> Color.Black
    }
)