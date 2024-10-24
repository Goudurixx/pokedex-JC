package com.goudurixx.pokedex.features.pokemon.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.routing.models.Routes
import com.goudurixx.pokedex.core.routing.utils.navigateSafely
import com.goudurixx.pokedex.features.pokemon.PokemonResultListRoute

internal const val pokemonFilterByParameterParam = "pokemon_filter_by_parameter"
internal const val pokemonFilterByValParam = "pokemon_filter_by_val_param"
internal const val pokemonResultNameParam = "pokemon_result_name"
internal const val pokemonResultColorParam = "pokemon_result_color"

private val pokemonResultListRoute = Routes.RESULT.route

internal class PokemonResultListArgs(
    val filterByParam: FilterByParameter,
    val filterByVal: String,
    val resultName: String,
    val resultColor: Int? = null
) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[pokemonFilterByParameterParam]) as FilterByParameter,
                checkNotNull(savedStateHandle[pokemonFilterByValParam]) as String,
                checkNotNull(savedStateHandle[pokemonResultNameParam]) as String,
                if (savedStateHandle.get<Int?>(pokemonResultColorParam) != null) savedStateHandle.get<Int>(
                    pokemonResultColorParam
                ) else null
            )
}

fun NavController.navigateToPokemonResultList(
    filterByParam: FilterByParameter,
    filterByVal: String,
    resultName: String,
    resultColor: Int? = null,
    navOptions: NavOptions? = null
) {
    this.navigateSafely(
        "$pokemonResultListRoute$pokemonFilterByParameterParam=$filterByParam$pokemonFilterByValParam=$filterByVal$pokemonResultNameParam=$resultName?$pokemonResultColorParam=$resultColor",
        navOptions
    )
}

internal fun NavGraphBuilder.pokemonResultList(
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
) {
    composable(route = "$pokemonResultListRoute$pokemonFilterByParameterParam={$pokemonFilterByParameterParam}$pokemonFilterByValParam={$pokemonFilterByValParam}$pokemonResultNameParam={$pokemonResultNameParam}?$pokemonResultColorParam={$pokemonResultColorParam}",
        arguments = listOf(
            navArgument(pokemonFilterByParameterParam) {
                type = NavType.EnumType(FilterByParameter::class.java)
            },
            navArgument(pokemonFilterByValParam) { type = NavType.StringType },
            navArgument(pokemonResultNameParam) { type = NavType.StringType },
            navArgument(pokemonResultColorParam) {
                type = NavType.IntType
                defaultValue = -1
            }
        )) {
        val resultType = it.arguments?.get(pokemonFilterByParameterParam) as FilterByParameter
        val resultName = it.arguments?.getString(pokemonResultNameParam) ?: ""
        val resultColor = it.arguments?.getInt(pokemonResultColorParam).let {color -> if (color != -1) color else null }
        PokemonResultListRoute(
            resultType = resultType,
            resultName = resultName,
            resultColor = resultColor,
            navigateToPokemonDetail = navigateToPokemonDetail,
            onBackClick = onBackClick
        )
    }
}
