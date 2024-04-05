package com.goudurixx.pokedex.graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.goudurixx.pokedex.core.routing.models.Graph
import com.goudurixx.pokedex.features.favorites.navigation.favoriteScreen
import com.goudurixx.pokedex.features.home.navigation.homeScreen
import com.goudurixx.pokedex.features.pokemap.navigation.pokeMap
import com.goudurixx.pokedex.features.pokemon.navigation.navigateToPokemonDetail
import com.goudurixx.pokedex.features.pokemon.navigation.navigateToPokemonList
import com.goudurixx.pokedex.features.pokemon.navigation.navigateToPokemonResultList
import com.goudurixx.pokedex.features.pokemon.navigation.pokemonDetailScreen
import com.goudurixx.pokedex.features.pokemon.navigation.pokemonList
import com.goudurixx.pokedex.features.pokemon.navigation.pokemonResultList
import com.goudurixx.pokedex.features.pokemon.navigation.pokemonType
import com.goudurixx.pokedex.navigation.MainDestinations

@Composable
fun MainNavGraph(
    navController: NavHostController
) {
    val transitionDuration = 500
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = MainDestinations.HOME.route,
        enterTransition = {
            MainDestinations.entries.find { it.route == initialState.destination.route }
                ?.let { initialDestination ->
                    MainDestinations.entries.find { it.route == navController.currentDestination?.route }
                        ?.let { currentDestination ->
                            if (initialDestination.ordinal < currentDestination.ordinal) {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(transitionDuration)
                                )
                            } else {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(transitionDuration)
                                )
                            }
                        }
                } ?: slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(transitionDuration)
            )
        },
        exitTransition = {
            MainDestinations.entries.find { it.route == initialState.destination.route }
                ?.let { initialDestination ->
                    MainDestinations.entries.find { it.route == navController.currentDestination?.route }
                        ?.let { currentDestination ->
                            if (initialDestination.ordinal < currentDestination.ordinal) {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(transitionDuration)
                                )
                            } else {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(transitionDuration)
                                )
                            }
                        }
                } ?: slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(transitionDuration)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(transitionDuration)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(transitionDuration)
            )
        }
    ) {
        homeScreen(
            navigateToPokemonList = {
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
                navController.navigateToPokemonList(topLevelNavOptions)
            },
            navigateToPokemonDetail = navController::navigateToPokemonDetail,
            navigateToPokemonFavorite =
            {
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
                navController.navigateToFavorite(topLevelNavOptions)
            },
            navigateToPokemonResultList = navController::navigateToPokemonResultList

        )
        pokemonList(navigateToPokemonDetail = navController::navigateToPokemonDetail)
        pokemonDetailScreen(
            onBackClick = {
                while (navController.popBackStack()) {
                    if (MainDestinations.entries.find { navController.currentDestination?.route == it.route } != null) break
                }
                navController.popBackStack(MainDestinations.POKEMON.route, inclusive = false)
            },
            navigateToPokemonDetail = navController::navigateToPokemonDetail,
            navigateToPokemonResultList = navController::navigateToPokemonResultList
        )
        favoriteScreen()
        pokeMap(
            onBackClick = navController::popBackStack,
            navigateToPokemonDetail = navController::navigateToPokemonDetail,
        )
        pokemonType(
            navigateToPokemonDetail = navController::navigateToPokemonDetail,
            onBackClick = navController::popBackStack,
        )
        pokemonResultList(
            navigateToPokemonDetail = navController::navigateToPokemonDetail,
            onBackClick = navController::popBackStack
        )
    }
}
