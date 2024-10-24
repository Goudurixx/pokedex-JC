package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GenerationIiResponse(
    val crystal: CrystalResponse,
    val gold: GoldResponse,
    val silver: SilverResponse
)