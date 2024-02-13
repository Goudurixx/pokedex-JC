package com.goudurixx.pokedex.core.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goudurixx.pokedex.R

val RubikFontFamily = FontFamily(
    listOf(
        Font(R.font.rubik_light, FontWeight.W300),
        Font(R.font.rubik_regular, FontWeight.W400),
        Font(R.font.rubik_medium, FontWeight.W500),
        Font(R.font.rubik_bold, FontWeight.W600)
    )
)

val Typography = Typography(

    // Display Large - Rubik 57/64 . 0
    displayLarge = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (0).sp,
    ),

    // Display Medium - Rubik 45/52 .  0
    displayMedium = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),

    // Display Small - Rubik 36/44 . 0
    displaySmall = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),

    // Headline Large - Rubik Medium 32/40 . 0
    headlineLarge = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),

    // Headline Medium - Rubik Medium 28/36 . 0
    headlineMedium = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),

    // Headline Small - Rubik Medium 24/32 . 0
    headlineSmall = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),

    // Title Large - Rubik Medium 22/28 . 0
    titleLarge = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),

    // Title Medium - Rubik Medium 16/24 . +0.15
    titleMedium = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),

    // Title Small - Rubik Medium 14/14 . +0.1
    titleSmall = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.1.sp,
    ),

    // Label Large - Roboto Medium 14/20 . +0.1
    labelLarge = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    // Label Medium - Roboto Medium 12/16 . +0.5
    labelMedium = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),

    // Label Small - Roboto Medium 11/16 . +0.5
    labelSmall = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),

    // Body Large - Roboto 16/24 . +0.5
    bodyLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),

    // Body Medium - Roboto 14/20 . +0.25
    bodyMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    // Body Small - Roboto 12/16 . +0.4
    bodySmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
)

@Composable
@Preview("preview of typo", showBackground = true )
fun PreviewTypography(){
    val cardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.onBackground)
    val borderStroke =  BorderStroke(width = 1.dp, color = Color.Gray)
    val modifier = Modifier.padding(4.dp)
    Column(Modifier.background(MaterialTheme.colorScheme.background)) {
        Card(modifier = modifier, colors = cardColors, border = borderStroke) {
            Text(text = "Display Large - Rubik 57/64 . 0  ", style = MaterialTheme.typography.displayLarge)
            Text(text = "Display Medium - Rubik 45/52 .  0  ", style = MaterialTheme.typography.displayMedium)
            Text(text = "Display Small - Rubik 36/44 . 0 ", style = MaterialTheme.typography.displaySmall)
        }
        Card(modifier = modifier, colors = cardColors, border = borderStroke) {
            Text(text = "Headline Large - Rubik Medium 32/40 . 0  ", style = MaterialTheme.typography.headlineLarge)
            Text(text = "Headline Medium - Rubik Medium 28/36 . 0  ", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Headline Small - Rubik Medium 24/32 . 0", style = MaterialTheme.typography.headlineSmall)

        }
        Card(modifier = modifier, colors = cardColors, border = borderStroke) {
            Text(text = "Title Large - Rubik Medium 22/28 . 0  ", style = MaterialTheme.typography.titleLarge)
            Text(text = "Title Medium - Rubik Medium 16/24 . +0.15", style = MaterialTheme.typography.titleMedium)
            Text(text = "Title Small - Rubik Medium 14/14 . +0.1  ", style = MaterialTheme.typography.titleSmall)

        }
        Card(modifier = modifier, colors = cardColors, border = borderStroke) {
            Text(text = "Label Large - Roboto Medium 14/20 . +0.1  ", style = MaterialTheme.typography.labelLarge)
            Text(text = "Label Medium - Roboto Medium 12/16 . +0.5", style = MaterialTheme.typography.labelMedium)
            Text(text = "Label Small - Roboto Medium 11/16 . +0.5", style = MaterialTheme.typography.labelSmall)
        }
        Card(modifier = modifier, colors = cardColors, border = borderStroke) {
            Text(text = "Body Large - Roboto 16/24 . +0.5 ", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Body Medium - Roboto 14/20 . +0.25 ", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Body Small - Roboto 12/16 . +0.4 ", style = MaterialTheme.typography.bodySmall)
        }
    }
}