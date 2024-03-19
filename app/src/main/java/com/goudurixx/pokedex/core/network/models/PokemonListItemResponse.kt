package com.goudurixx.pokedex.core.network.models

import com.goudurixx.pokedex.PokemonGetPagedListQuery
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListItemResponse(
    val id: Int? = null,
    val name: String,
    val url: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val baseExperience: Int? = null,
    val averageStat : Double? = null,
    val colorId: Int? = null,
    val generationId : Int? = null,
    val generationName : String? = null
)

fun PokemonGetPagedListQuery.Pokemon_v2_pokemon.toResponseModel() = PokemonListItemResponse(
    id = id,
    name = name,
    height = height,
    weight = weight,
    baseExperience = base_experience,
    averageStat = avg_stat_and_color.pokemon_v2_pokemonstats_aggregate.aggregate?.avg?.base_stat,
    colorId = avg_stat_and_color.pokemon_v2_pokemonspecy!!.pokemon_v2_pokemoncolor!!.id,
    generationId = generation_id.pokemon_v2_pokemonspecy?.pokemon_v2_generation?.id,
    generationName = generation_id.pokemon_v2_pokemonspecy?.pokemon_v2_generation?.name
)