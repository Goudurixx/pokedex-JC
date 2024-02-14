package com.goudurixx.pokedex.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeRoute(navigateToPokemonList: () -> Unit) {

    HomeScreen(navigateToPokemonList = navigateToPokemonList)
}

@Composable
fun HomeScreen(navigateToPokemonList: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "Home Screen")
            OutlinedButton(onClick = navigateToPokemonList) {
                Text(text = "Go to Pokemon List")
            }
        }
    }
}