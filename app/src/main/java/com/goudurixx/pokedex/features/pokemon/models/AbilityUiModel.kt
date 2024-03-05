package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.AbilityModel

data class AbilityUiModel (
    val name: String,
    val isHidden: Boolean,
    val slot: Int
) {
    companion object {
        fun placeHolder() = AbilityUiModel(
            name = "Loading...",
            isHidden = false,
            slot = 0
        )
    }
}

fun AbilityModel.toUiModel() = AbilityUiModel(
    name = name,
    isHidden = isHidden,
    slot = slot
)