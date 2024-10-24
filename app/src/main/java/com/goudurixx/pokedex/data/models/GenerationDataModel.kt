package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.GenerationResponse

data class GenerationDataModel(
    val id: Int,
    val name: String
)

fun GenerationResponse.toDataModel() = GenerationDataModel(
    id = id,
    name = name
)
