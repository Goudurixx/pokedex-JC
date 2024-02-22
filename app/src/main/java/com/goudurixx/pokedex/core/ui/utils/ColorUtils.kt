package com.goudurixx.pokedex.core.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

fun Color.getContrastingColor() : Color{

    return this.takeIf { it.luminance() > 0.5f }
        ?.let { Color.Black } ?: Color.White
}