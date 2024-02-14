package com.goudurixx.pokedex.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.goudurixx.pokedex.core.routing.models.Graph
import com.goudurixx.pokedex.features.home.navigation.homeScreen
import com.goudurixx.pokedex.features.pokemon.navigation.navigateToPokemonDetail
import com.goudurixx.pokedex.features.pokemon.navigation.navigateToPokemonList
import com.goudurixx.pokedex.features.pokemon.navigation.pokemonDetailScreen
import com.goudurixx.pokedex.features.pokemon.navigation.pokemonList
import com.goudurixx.pokedex.navigation.MainDestinations

@Composable
fun MainNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = MainDestinations.HOME.route
    ) {
        homeScreen(
            navigateToPokemonList = {
                navController.navigateToPokemonList()
            }
        )
        pokemonList(
            onBackClick = {
                navController.popBackStack()
            },
            navigateToPokemonDetail = { pokemonId ->
                navController.navigateToPokemonDetail(id = pokemonId)
            }
        )
        pokemonDetailScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}