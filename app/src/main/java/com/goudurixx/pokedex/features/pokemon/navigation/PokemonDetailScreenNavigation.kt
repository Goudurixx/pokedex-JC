package com.goudurixx.pokedex.features.pokemon.navigation

import android.util.Log
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

internal const val pokemonIdParam = "id"
internal const val pokemonColorParam = "color"

private val pokemonDetailRoute = Routes.POKEMON.route + "?"

internal class PokemonArgs(val id: Int, val color: Int? = null) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[pokemonIdParam]) as Int,
                if (savedStateHandle.get<Int?>(pokemonColorParam) != null) savedStateHandle.get<Int>(
                    pokemonColorParam
                )
                else null
            )
}

fun NavController.navigateToPokemonDetail(id: Int, color: Int? = null, navOptions: NavOptions? = null) {
    this.navigateSafely("$pokemonDetailRoute$pokemonIdParam=$id$pokemonColorParam=$color", navOptions)
}

internal fun NavGraphBuilder.pokemonDetailScreen(
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (Int, Int?) -> Unit
) {
    composable(
        route = "$pokemonDetailRoute$pokemonIdParam={$pokemonIdParam}$pokemonColorParam={$pokemonColorParam}",
        arguments = listOf(
            navArgument(pokemonIdParam) { type = NavType.IntType },
            navArgument(pokemonColorParam) {
                type = NavType.IntType
            }
        )
    ) {
        val color = it.arguments?.getInt(pokemonColorParam)
        Log.e("PokemonDetailScreenNavigation", "color: $color")
        PokemonDetailRoute(
            onBackClick = onBackClick,
            backgroundColor = color,
            navigateToPokemonDetail = { id, pokemonColor -> if(id != it.arguments?.getInt(pokemonIdParam)) navigateToPokemonDetail(id, pokemonColor) }
        )
    }
}