package com.goudurixx.pokedex.features.pokemon.models

data class GenerationUiModel (
    override val id: Int,
    val name: String,
) : ListFilterItemUiModel()

enum class Generations(val id: Int, val generationName: String) {
    GEN_1(1, "I"),
    GEN_2(2, "II"),
    GEN_3(3, "III"),
    GEN_4(4, "IV"),
    GEN_5(5, "V"),
    GEN_6(6, "VI"),
    GEN_7(7, "VII"),
    GEN_8(8, "VIII"),
}