package com.goudurixx.pokedex.core.network.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GenerationResponse(
    val id: Int,
    val name: String
)