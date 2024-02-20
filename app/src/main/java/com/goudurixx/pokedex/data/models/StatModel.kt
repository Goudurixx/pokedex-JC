package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.StatResponse
import com.goudurixx.pokedex.core.utils.trimIntFromUrl

data class StatModel(
    val statName: String,
    val statId : Int,
    val value: Int,
    val effort: Int,
)

fun StatResponse.toDataModel() = StatModel(
    statName = stat.name,
    statId = stat.url.trimIntFromUrl(),
    value = base_stat,
    effort = effort,
)