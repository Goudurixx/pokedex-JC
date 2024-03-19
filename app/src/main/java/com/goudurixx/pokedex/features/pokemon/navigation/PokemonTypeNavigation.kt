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
import com.goudurixx.pokedex.features.pokemon.PokemonTypeRoute

internal const val pokemonTypeIdParam = "type_id"
internal const val pokemonTypeNameParam = "type_name"

private val pokemonTypeRoute = Routes.TYPE.route

internal class TypeArgs(val typeId: Int, val typeName: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[pokemonTypeIdParam]) as Int,
                checkNotNull(savedStateHandle[pokemonTypeNameParam]) as String
            )
}

fun NavController.navigateToPokemonType(typeId: Int, typeName:String, navOptions: NavOptions? = null) {
    this.navigateSafely("$pokemonTypeRoute$pokemonTypeIdParam=$typeId$pokemonTypeNameParam=$typeName", navOptions)
}

internal fun NavGraphBuilder.pokemonType(
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick : () -> Unit,
) {
    composable(route = "$pokemonTypeRoute$pokemonTypeIdParam={$pokemonTypeIdParam}$pokemonTypeNameParam={$pokemonTypeNameParam}",
        arguments = listOf(
            navArgument(pokemonTypeIdParam) { type = NavType.IntType },
            navArgument(pokemonTypeNameParam) { type = NavType.StringType }
        )) {
        val typeName = it.arguments?.getString(pokemonTypeNameParam) ?: ""
        PokemonTypeRoute(
            typeName = typeName,
            navigateToPokemonDetail = navigateToPokemonDetail,
            onBackClick = onBackClick
        )
    }
}