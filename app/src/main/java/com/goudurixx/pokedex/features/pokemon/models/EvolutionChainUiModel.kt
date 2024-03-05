package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.EvolutionChainModel
import com.goudurixx.pokedex.data.models.EvolutionChainSpeciesModel

data class EvolutionChainUiModel(
    val id: Int,
    val evolutionChain: Map<Int, EvolutionChainSpeciesUiModel>)

data class EvolutionChainSpeciesUiModel(
    val id : Int,
    val evolveFromId: Int?,
    val name: String,
    val imageUrl: String)

fun EvolutionChainModel.toUiModel() = EvolutionChainUiModel(
    id = id,
    evolutionChain = evolutionChain.mapValues { it.value.toUiModel() }
)

fun EvolutionChainSpeciesModel.toUiModel() = EvolutionChainSpeciesUiModel(
    id = id,
    evolveFromId = evolveFromId,
    name = name,
    imageUrl = imageUrl
)