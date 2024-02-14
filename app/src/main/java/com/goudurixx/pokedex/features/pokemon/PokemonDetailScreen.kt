package com.goudurixx.pokedex.features.pokemon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PokemonDetailRoute(onBackClick: () -> Unit) {

    PokemonDetailScreen()
}

@Composable
fun PokemonDetailScreen() {
   Text(text = "Hello pokemon")
}