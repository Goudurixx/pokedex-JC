package com.goudurixx.pokedex.features.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.goudurixx.pokedex.core.routing.models.Routes
import com.goudurixx.pokedex.core.routing.utils.navigateSafely
import com.goudurixx.pokedex.features.favorites.FavoriteRoute

private val favoriteRoute = Routes.FAVORITE.route

fun NavController.navigateToFavorite(navOptions: NavOptions? = null) {
    this.navigateSafely(favoriteRoute, navOptions)
}

fun NavGraphBuilder.favoriteScreen(
) {
    composable(route = favoriteRoute) {
        FavoriteRoute()
    }
}