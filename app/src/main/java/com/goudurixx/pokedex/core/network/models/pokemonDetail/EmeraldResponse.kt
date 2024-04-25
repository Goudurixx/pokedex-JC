package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class EmeraldResponse(
    val front_default: String? = null,
    val front_shiny: String? = null
)