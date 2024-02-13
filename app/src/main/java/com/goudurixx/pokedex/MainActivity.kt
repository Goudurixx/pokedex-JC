package com.goudurixx.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.goudurixx.pokedex.core.ui.theme.PokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list_screen",
                ) {
                    composable("pokemon_list_screen") {
//                        PokemonListScreen(navController = navController)
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text(
                                    text = "Hello, Pokedex!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                                OutlinedButton(onClick = {
                                    val dominantColor =
                                        Color.Yellow.toArgb() // replace with actual color
                                    val pokemonName = "Pikachu" // replace with actual pokemon name
                                    navController.navigate("pokemon_detail_screen/$dominantColor/$pokemonName")
                                }) {
                                    Text(text = "To Detail Screen")
                                }
                            }
                        }
                    }
                    composable(
                        "pokemon_detail_screen/{dominantColor}/{pokemonName}", arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("pokemonName") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(dominantColor)
                        ) {
                            Text(
                                text = pokemonName ?: "Pokemon Detail Screen",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
//                        PokemonDetailScreen(navController = navController)
                    }
                }
            }
        }
    }
}