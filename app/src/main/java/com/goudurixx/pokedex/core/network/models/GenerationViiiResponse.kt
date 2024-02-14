package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class GenerationViiiResponse(
    val icons: IconsResponse
)