package com.goudurixx.pokedex.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.routing.models.Routes

enum class MainDestinations(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val title: Int,
    val route: String,
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        title = R.string.main_screen_title,
        route = Routes.HOME.route
    ),
    POKEMON(
        selectedIcon = Icons.Filled.Animation,
        unselectedIcon = Icons.Outlined.Circle,
        title = R.string.pokemon_list_screen_title,
        route = Routes.POKEMON.route
    ),
    FAVORITE(
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Filled.FavoriteBorder,
        title = R.string.favorite_screen_title,
        route = Routes.FAVORITE.route
    ),
}