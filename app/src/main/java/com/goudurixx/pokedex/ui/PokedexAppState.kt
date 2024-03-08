package com.goudurixx.pokedex.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.goudurixx.pokedex.core.data.util.NetworkMonitor
import com.goudurixx.pokedex.features.favorites.navigation.navigateToFavorite
import com.goudurixx.pokedex.features.home.navigation.navigateToHome
import com.goudurixx.pokedex.features.pokemon.navigation.navigateToPokemonList
import com.goudurixx.pokedex.navigation.MainDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberPokedexAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): PokedexAppState {
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
    ) {
        PokedexAppState(
            navController,
            coroutineScope,
            windowSizeClass,
            networkMonitor,
        )
    }
}

@Stable
class PokedexAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
       get() = !shouldShowBottomBar

    val isFullScreen : Boolean
       @Composable get() = MainDestinations.entries.map { it.route }.contains(currentDestination?.route)

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<MainDestinations> = MainDestinations.entries


    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: MainDestinations) {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

        when (topLevelDestination) {
            MainDestinations.HOME -> navController.navigateToHome(topLevelNavOptions)
            MainDestinations.POKEMON -> navController.navigateToPokemonList(topLevelNavOptions)
            MainDestinations.FAVORITE -> navController.navigateToFavorite(topLevelNavOptions)
        }
    }
}
