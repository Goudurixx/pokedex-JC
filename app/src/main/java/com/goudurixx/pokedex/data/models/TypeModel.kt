package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.TypeResponse
import com.goudurixx.pokedex.core.utils.trimIntFromUrl

data class TypeModel(
    val id: Int,
    val type: String,
)

fun TypeResponse.toDataModel() = TypeModel(
    id = type.url.trimIntFromUrl(),
    type = type.name,
)
