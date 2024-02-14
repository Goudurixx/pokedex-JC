package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse(
    val abilities: List<AbilityResponse>,
    val base_experience: Int,
    val cries: CriesResponse,
    val forms: List<FormResponse>,
    val game_indices: List<GameIndiceResponse>,
    val height: Int,
    //val held_items: List<Any>, TODO - Implement this
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<MoveResponse>,
    val name: String,
    val order: Int,
    val past_abilities: List<AbilityResponse>,
    val past_types: List<TypeResponse>,
    val species: SpeciesResponse,
    val sprites: SpritesResponse,
    val stats: List<StatResponse>,
    val types: List<TypeResponse>,
    val weight: Int
)