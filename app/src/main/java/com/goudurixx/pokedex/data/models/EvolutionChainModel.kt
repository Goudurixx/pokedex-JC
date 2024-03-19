package com.goudurixx.pokedex.data.models

import com.goudurixx.pokedex.core.network.models.EvolutionChainResponse
import com.goudurixx.pokedex.core.network.models.EvolutionChainSpeciesResponse

data class EvolutionChainModel(
    val id: Int,
    val evolutionChain: Map<Int, EvolutionChainSpeciesModel>
)

data class EvolutionChainSpeciesModel(
    val id : Int,
    val evolveFromId: Int?,
    val name: String,
    val imageUrl: String,
    val color : Int?
)

fun EvolutionChainResponse.toDataModel() = EvolutionChainModel(
    id = id,
    evolutionChain = pokemon_v2_pokemonspecies.associateBy(
        { it.id },
        { it.toDataModel() }
    )
)

fun EvolutionChainSpeciesResponse.toDataModel() = EvolutionChainSpeciesModel(
    id = id,
    evolveFromId = evolves_from_species_id,
    name = name,
    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png",
    color = color
)