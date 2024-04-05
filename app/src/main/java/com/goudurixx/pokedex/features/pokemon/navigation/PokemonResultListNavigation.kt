package com.goudurixx.pokedex.features.pokemon.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.routing.models.Routes
import com.goudurixx.pokedex.core.routing.utils.navigateSafely
import com.goudurixx.pokedex.features.pokemon.PokemonResultListViewModel
import com.goudurixx.pokedex.features.pokemon.ResultListUiState
import com.goudurixx.pokedex.features.pokemon.components.PokemonList
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import java.util.Locale

internal const val pokemonFilterByParameterParam = "pokemon_filter_by_parameter"
internal const val pokemonFilterByValParam = "pokemon_filter_by_val_param"
internal const val pokemonResultNameParam = "pokemon_result_name"
internal const val pokemonResultColorParam = "pokemon_result_color"

private val pokemonResultListRoute = Routes.RESULT.route

internal class PokemonResultListArgs(
    val filterByParam: FilterByParameter,
    val filterByVal: Int,
    val resultName: String,
    val resultColor: Int? = null
) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[pokemonFilterByParameterParam]) as FilterByParameter,
                checkNotNull(savedStateHandle[pokemonFilterByValParam]) as Int,
                checkNotNull(savedStateHandle[pokemonResultNameParam]) as String,
                if (savedStateHandle.get<Int?>(pokemonResultColorParam) != null) savedStateHandle.get<Int>(
                    pokemonResultColorParam
                ) else null
            )
}

fun NavController.navigateToPokemonResultList(
    filterByParam: FilterByParameter,
    filterByVal: Int,
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
            navArgument(pokemonFilterByValParam) { type = NavType.IntType },
            navArgument(pokemonResultNameParam) { type = NavType.StringType },
            navArgument(pokemonResultColorParam) {
                type = NavType.IntType
                defaultValue = -1
            }
        )) {
        val resultType = it.arguments?.get(pokemonFilterByParameterParam) as FilterByParameter
        val resultName = it.arguments?.getString(pokemonResultNameParam) ?: ""
        val resultColor = it.arguments?.getInt(pokemonResultColorParam)
        PokemonResultListRoute(
            resultType = resultType,
            resultName = resultName,
            resultColor = resultColor,
            navigateToPokemonDetail = navigateToPokemonDetail,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun PokemonResultListRoute(
    resultType: FilterByParameter,
    resultName: String,
    resultColor: Int?,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PokemonResultListViewModel = hiltViewModel(),
) {

    val pokemonLazyPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PokemonResultListScreen(
        uiState = uiState,
        resultType = resultType,
        resultName = resultName,
        resultColor = resultColor,
        pokemonLazyPagingItems = pokemonLazyPagingItems,
        onUpdateFavorite = viewModel::updateFavorite,
        navigateToPokemonDetail = navigateToPokemonDetail,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonResultListScreen(
    uiState: ResultListUiState,
    resultType: FilterByParameter,
    resultName: String,
    resultColor: Int?,
    pokemonLazyPagingItems: LazyPagingItems<PokemonListItemUiModel>,
    onUpdateFavorite: (Int, Boolean) -> Unit,
    navigateToPokemonDetail: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = resultName.capitalize(Locale.ROOT)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            })
    }) {
        when (uiState) {
            is ResultListUiState.Success -> Box(modifier = Modifier.background(resultColor?.let {
                Color(
                    it
                )
            } ?: MaterialTheme.colorScheme.background)) {
                PokemonList(
                    pokemonLazyPagingItems = pokemonLazyPagingItems,
                    onItemClick = navigateToPokemonDetail,
                    onUpdateFavorite = onUpdateFavorite,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.padding(it),
                )
                Text(text = resultName)
            }


            is ResultListUiState.Loading -> CircularProgressIndicator()
            is ResultListUiState.Error -> Text("Error loading data")
        }
    }
}