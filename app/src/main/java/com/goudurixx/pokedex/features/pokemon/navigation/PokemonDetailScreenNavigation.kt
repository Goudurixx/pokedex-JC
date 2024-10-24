package com.goudurixx.pokedex.features.pokemon.navigation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.goudurixx.pokedex.core.common.models.FilterByParameter
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

fun NavController.navigateToPokemonDetail(
    id: Int,
    color: Int? = null,
    navOptions: NavOptions? = null
) {
    this.navigateSafely(
        "$pokemonDetailRoute$pokemonIdParam=$id$pokemonColorParam=$color",
        navOptions
    )
}

internal fun NavGraphBuilder.pokemonDetailScreen(
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (Int, Int?) -> Unit,
    navigateToPokemonResultList: (FilterByParameter, String, String, Int?) -> Unit
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
        val id = it.arguments?.getInt(pokemonIdParam)
        val color = it.arguments?.getInt(pokemonColorParam)
        val lifecycleOwner = LocalLifecycleOwner.current

        PokemonDetailRoute(
            onBackClick = {
                val currentState = lifecycleOwner.lifecycle.currentState
                if (currentState.isAtLeast(Lifecycle.State.RESUMED)) onBackClick()
            },
            pokemonId = id,
            backgroundColor = color,
            navigateToPokemonDetail = { destId, pokemonColor ->
                if (destId != it.arguments?.getInt(
                        pokemonIdParam
                    )
                ) navigateToPokemonDetail(destId, pokemonColor)
            },
            navigateToPokemonResultList = navigateToPokemonResultList
        )
    }
}