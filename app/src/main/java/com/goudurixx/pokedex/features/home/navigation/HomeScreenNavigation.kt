package com.goudurixx.pokedex.features.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.routing.models.Routes
import com.goudurixx.pokedex.core.routing.utils.navigateSafely
import com.goudurixx.pokedex.features.home.HomeRoute

private val homeRoute = Routes.HOME.route

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigateSafely(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    navigateToPokemonList : () -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    navigateToPokemonFavorite: () -> Unit,
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit
) {
    composable(route = homeRoute) {
        HomeRoute(
           navigateToPokemonList = navigateToPokemonList,
              navigateToPokemonDetail = navigateToPokemonDetail,
                navigateToPokemonFavorite = navigateToPokemonFavorite,
                navigateToPokemonResultList = navigateToPokemonResultList
        )
    }
}