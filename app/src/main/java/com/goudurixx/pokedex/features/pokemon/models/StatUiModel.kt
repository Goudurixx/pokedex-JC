package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.StatModel

data class StatUiModel(
    val statName: String,
    val statId: Int,
    val value: Int,
    val effort: Int
)

fun StatModel.toUiModel() = StatUiModel(
    statName = statName,
    statId = statId,
    value = value,
    effort = effort
)