package com.goudurixx.pokedex.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.graphics.vector.ImageVector
import com.goudurixx.pokedex.R
import com.goudurixx.pokedex.core.routing.models.Routes

enum class MainDestinations(
    val icon: ImageVector,
    val title: Int,
    val route: String,
) {
    POKEMAP(
        icon = Icons.Filled.Map,
        title = R.string.app_name,
        route = Routes.POKEMAP.route
    ),
    HOME(
        icon = Icons.Filled.Home,
        title = R.string.app_name,
        route = Routes.HOME.route
    ),
    POKEMON(
        icon = Icons.Filled.Animation,
        title = R.string.app_name,
        route = Routes.POKEMON.route
    )
}