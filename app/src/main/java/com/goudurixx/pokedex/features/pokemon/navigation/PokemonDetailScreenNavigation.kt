package com.goudurixx.pokedex.features.pokemon.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.goudurixx.pokedex.core.routing.models.Routes
import com.goudurixx.pokedex.core.routing.utils.navigateSafely
import com.goudurixx.pokedex.features.pokemon.PokemonDetailRoute

internal const val pokemonArg = "id"

private val pokemonDetailRoute = Routes.POKEMON.route + "?"

internal class PokemonArgs(val id: Int) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(checkNotNull(savedStateHandle[pokemonArg]) as Int)
}

fun NavController.navigateToPokemonDetail(id: Int, navOptions: NavOptions? = null) {
    this.navigateSafely("$pokemonDetailRoute$pokemonArg=$id", navOptions)
}

internal fun NavGraphBuilder.pokemonDetailScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "$pokemonDetailRoute$pokemonArg={$pokemonArg}",
        arguments = listOf(navArgument(pokemonArg) { type = NavType.IntType })
    ) {
        PokemonDetailRoute(
            onBackClick = onBackClick,
        )
    }
}