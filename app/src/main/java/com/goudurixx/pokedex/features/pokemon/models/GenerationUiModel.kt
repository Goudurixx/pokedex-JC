package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.GenerationDataModel

data class GenerationUiModel (
    override val id: Int,
    val name: String,
) : ListFilterItemUiModel()
fun GenerationDataModel.toUiModel() = GenerationUiModel(
    id = id,
    name = name.replace("generation-", "").uppercase()
)
