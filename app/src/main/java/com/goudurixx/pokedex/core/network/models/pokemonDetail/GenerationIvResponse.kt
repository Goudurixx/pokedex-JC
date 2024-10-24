package com.goudurixx.pokedex.core.network.models.pokemonDetail

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GenerationIvResponse(
    @SerialName("diamond-pearl") val diamond_pearl: DiamondPearlResponse,
    @SerialName("heartgold-soulsilver")val heartgold_soulsilver: HeartgoldSoulsilverResponse,
    val platinum: PlatinumResponse
)