package com.goudurixx.pokedex.core.ui.component.radarChart

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import com.goudurixx.pokedex.core.ui.component.radarChart.models.RadarChartConfig

internal fun DrawScope.drawAxisData(
    labelsStyle: TextStyle,
    scalarValuesStyle: TextStyle,
    textMeasurer: TextMeasurer,
    radarChartConfig: RadarChartConfig,
    radarLabels: List<String>,
    scalarValue: Double,
    scalarSteps: Int,
    unit: String
) {

    val labelsEndPoints = radarChartConfig.labelsPoints
    val nextStartPoints = radarChartConfig.polygonPoints.toMutableList()
    nextStartPoints.add(0, center)
    nextStartPoints.removeAt(nextStartPoints.size - 1)

    val scalarStep = scalarValue / (scalarSteps - 1)
    val textVerticalOffset = 20.toDp().toPx()
    val labelHeight = textMeasurer.measure(AnnotatedString("M")).size.height
    val bottomElement = radarLabels.size / 2

    // AXIS LABELS
    for (step in 0 until scalarSteps) {
        drawText(
            textMeasurer = textMeasurer,
            text = (scalarStep * step).toString() + " " + unit,
            style = scalarValuesStyle,
            topLeft = Offset(
                nextStartPoints[step].x + 5.toDp().toPx(),
                nextStartPoints[step].y - textVerticalOffset
            )
        )
    }

    // RADAR LABELS
    for (line in labelsEndPoints.indices) {
        drawText(
            textMeasurer = textMeasurer,
            text = radarLabels[line],
            style = labelsStyle,
            topLeft = Offset(
                labelsEndPoints[line].x - textMeasurer.measure(
                    AnnotatedString(
                        text = radarLabels[line],
                    ), style = labelsStyle
                ).size.width / 2,
                labelsEndPoints[line].y - if (line == bottomElement) (labelHeight * 1.5f).toInt() else labelHeight / 2
            )
        )
    }

}