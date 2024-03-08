package com.goudurixx.pokedex.features.pokemon.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.goudurixx.pokedex.core.routing.models.Routes
import com.goudurixx.pokedex.core.routing.utils.navigateSafely
import com.goudurixx.pokedex.features.pokemon.PokemonListRoute

private val pokemonListRoute = Routes.POKEMON.route

fun NavController.navigateToPokemonList(navOptions: NavOptions? = null) {
    this.navigateSafely(pokemonListRoute, navOptions)
}

internal fun NavGraphBuilder.pokemonList(
    navigateToPokemonDetail: (Int, Int) -> Unit,
) {
    composable(route = pokemonListRoute) {
        PokemonListRoute(
            navigateToPokemonDetail = navigateToPokemonDetail,
        )
    }
}