package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class GenerationIiResponse(
    val crystal: CrystalResponse,
    val gold: GoldResponse,
    val silver: SilverResponse
)