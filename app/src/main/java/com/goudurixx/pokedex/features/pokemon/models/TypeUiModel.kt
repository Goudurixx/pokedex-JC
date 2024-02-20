package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.data.models.TypeModel
import  androidx.compose.ui.graphics.Color
import com.goudurixx.pokedex.core.ui.theme.*

data class TypeUiModel(
    val id: Int,
    val name: String,
    val color : Color
)

fun TypeModel.toUiModel() = TypeUiModel(
    id = id,
    name = type,
    color = TypeColor.entries.find { it.id == id }?.color ?: Color.Gray
)

enum class TypeColor(val id: Int, val color: Color) {
    NORMAL(id = 1, color = TypeNormal),
    FIGHTING(id = 2, color = TypeFighting),
    FLYING(id = 3, color = TypeFlying),
    POISON(id = 4, color = TypePoison),
    GROUND(id = 5, color = TypeGround),
    ROCK(id = 6, color = TypeRock),
    BUG(id = 7, color = TypeBug),
    GHOST(id = 8, color = TypeGhost),
    STEEL(id = 9, color = TypeSteel),
    FIRE(id = 10, color = TypeFire),
    WATER(id = 11, color = TypeWater),
    GRASS(id = 12, color = TypeGrass),
    ELECTRIC(id = 13, color = TypeElectric),
    PSYCHIC(id = 14, color = TypePsychic),
    ICE(id = 15, color = TypeIce),
    DRAGON(id = 16, color = TypeDragon),
    DARK(id = 17, color = TypeDark),
    FAIRY(id = 18, color = TypeFairy),
    UNKNOWN(id = 10001, color = TypeUnknown),
    SHADOW(id = 10002, color = TypeShadow)
}