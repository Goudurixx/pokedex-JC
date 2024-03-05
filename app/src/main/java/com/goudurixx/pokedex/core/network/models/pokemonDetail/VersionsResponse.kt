package com.goudurixx.pokedex.core.network.models.pokemonDetail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionsResponse(
    @SerialName("generation-i") val generation_i: GenerationIResponse,
    @SerialName("generation-ii") val generation_ii: GenerationIiResponse,
    @SerialName("generation-iii") val generation_iii: GenerationIiiResponse,
    @SerialName("generation-iv") val generation_iv: GenerationIvResponse,
    @SerialName("generation-v") val generation_v: GenerationVResponse,
    @SerialName("generation-vi") val generation_vi: GenerationViResponse,
    @SerialName("generation-vii") val generation_vii: GenerationViiResponse,
    @SerialName("generation-viii") val generation_viii: GenerationViiiResponse
)