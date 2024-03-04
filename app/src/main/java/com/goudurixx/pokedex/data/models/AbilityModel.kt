package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.pokemonDetail.AbilityResponse

data class AbilityModel(
    val name: String,
    val isHidden: Boolean,
    val slot: Int
)


fun AbilityResponse.toDataModel() = AbilityModel(
    name = ability.name,
    isHidden = is_hidden,
    slot = slot
)