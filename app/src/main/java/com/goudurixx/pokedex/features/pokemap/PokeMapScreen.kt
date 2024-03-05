package com.goudurixx.pokedex.features.pokemap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PokeMapRoute(
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit
) {
    PokeMapScreen()
}

@Composable
fun PokeMapScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "PokeMapScreen \uD83D\uDEA7")
    }
}