package com.goudurixx.pokedex.features.favorites

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun FavoriteRoute() {

    FavoriteScreen()
}

@Composable
fun FavoriteScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Text(text = "Favorite Screen", modifier = Modifier.padding(it))
    }
}
