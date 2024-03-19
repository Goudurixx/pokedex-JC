package com.goudurixx.pokedex.core.network.models

import com.goudurixx.pokedex.PokemonEvolutionChainQuery
import kotlinx.serialization.Serializable

@Serializable
data class EvolutionChainResponse(
    val id: Int,
    val pokemon_v2_pokemonspecies : List<EvolutionChainSpeciesResponse>,
)

@Serializable
data class EvolutionChainSpeciesResponse(
    val name : String,
    val id: Int,
    val evolves_from_species_id : Int? = null,
    val color : Int?
)

fun PokemonEvolutionChainQuery.Pokemon_v2_evolutionchain.toResponseModel() = EvolutionChainResponse(
    id = id,
    pokemon_v2_pokemonspecies = pokemon_v2_pokemonspecies.map { it.toResponseModel() }
)

fun PokemonEvolutionChainQuery.Pokemon_v2_pokemonspecy1.toResponseModel() = EvolutionChainSpeciesResponse(
    name = name,
    id = id,
    evolves_from_species_id = evolves_from_species_id,
    color = pokemon_color_id
)