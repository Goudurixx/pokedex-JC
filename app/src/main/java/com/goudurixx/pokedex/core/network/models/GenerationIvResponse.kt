package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationIvResponse(
    @SerialName("diamond-pearl") val diamond_pearl: DiamondPearlResponse,
    @SerialName("heartgold-soulsilver")val heartgold_soulsilver: HeartgoldSoulsilverResponse,
    val platinum: PlatinumResponse
)