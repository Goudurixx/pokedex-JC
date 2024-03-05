package com.goudurixx.pokedex.features.pokemap.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.goudurixx.pokedex.core.routing.models.Routes
import com.goudurixx.pokedex.core.routing.utils.navigateSafely
import com.goudurixx.pokedex.features.pokemap.PokeMapRoute

private val pokeMapRoute = Routes.POKEMAP.route

fun NavController.navigateToPokeMap(navOptions: NavOptions? = null) {
    this.navigateSafely(pokeMapRoute, navOptions)
}

internal fun NavGraphBuilder.pokeMap(
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
) {
    composable(route = pokeMapRoute) {
        PokeMapRoute(
            onBackClick = onBackClick,
            navigateToPokemonDetail = navigateToPokemonDetail,
        )
    }
}