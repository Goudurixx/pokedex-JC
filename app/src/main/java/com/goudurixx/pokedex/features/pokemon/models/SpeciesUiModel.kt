package com.goudurixx.pokedex.features.pokemon.models

data class SpeciesUiModel(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val abilities: List<String>,
    val stats: List<StatUiModel>,
    val imageUrl: String,
    val description: String
)