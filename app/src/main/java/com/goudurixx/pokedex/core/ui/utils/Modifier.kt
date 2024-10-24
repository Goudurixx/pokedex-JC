package com.goudurixx.pokedex.core.ui.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

fun Modifier.shimmerEffect(isLoading: Boolean = true): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000
            )
        ), label = "shimmerOffset"
    )

    if (isLoading) {
        background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFB9B8B8),
                    Color(0xFF747474),
                    Color(0xFFB9B8B8),
                ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width, size.height.toFloat()),
            )
        )
            .onGloballyPositioned {
                size = it.size
            }
    } else this
}