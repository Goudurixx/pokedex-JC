package com.goudurixx.pokedex.core.ui.component.radarChart.models

data class Polygon(
    val style: PolygonStyle,
    val values: List<Double>,
    val unit: String = "",
)