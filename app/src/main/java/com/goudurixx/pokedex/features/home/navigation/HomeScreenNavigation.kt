package com.goudurixx.pokedex.features.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.goudurixx.pokedex.core.routing.models.Routes
import com.goudurixx.pokedex.core.routing.utils.navigateSafely
import com.goudurixx.pokedex.features.home.HomeRoute

private val homeRoute = Routes.HOME.route

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigateSafely(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    navigateToPokemonList : () -> Unit
) {
    composable(route = homeRoute) {
        HomeRoute(
           navigateToPokemonList = navigateToPokemonList
        )
    }
}