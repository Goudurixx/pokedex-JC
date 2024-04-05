package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.FilterByParameter.*
import com.goudurixx.pokedex.core.common.models.ListFilterValue
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import com.goudurixx.pokedex.features.pokemon.navigation.PokemonResultListArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonResultListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pokemonRepository: IPokemonRepository,
) : ViewModel() {

    private val pokemonResultListArgs = PokemonResultListArgs(savedStateHandle)

    private val _uiState: MutableStateFlow<ResultListUiState> =
        MutableStateFlow(ResultListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val pokemonPagingFlow =
        pokemonRepository.getPokemonPagerList(
            null,
            listOf<FilterBy>(
                when (pokemonResultListArgs.filterByParam) {
                    TYPE -> FilterBy(
                        parameter = TYPE,
                        value = ListFilterValue(
                            value = listOf(pokemonResultListArgs.filterByVal),
                            type = TYPE
                        )
                    )
                    ID -> TODO()
                    HEIGHT -> TODO()
                    WEIGHT -> TODO()
                    BASE_EXPERIENCE -> TODO()
                    HP -> TODO()
                    DEFENSE -> TODO()
                    ATTACK -> TODO()
                    GENERATION -> FilterBy(
                        parameter = GENERATION,
                        value = ListFilterValue(
                            value = listOf(pokemonResultListArgs.filterByVal),
                            type = GENERATION
                        )
                    )

                    IS_LEGENDARY -> TODO()
                    IS_DEFAULT -> TODO()
                    IS_BABY -> TODO()
                    IS_MYTHICAL -> TODO()
                }
            )
        ).map { pagingData ->
            pagingData.map { it.toUiModel() }
        }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            _uiState.update {
                ResultListUiState.Success(pokemonResultListArgs)
            }
        }
    }

    fun updateFavorite(pokemonId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            pokemonRepository.updateFavorite(pokemonId, isFavorite)
        }
    }
}

sealed interface ResultListUiState {
    object Loading : ResultListUiState
    data class Success(val data: Any) : ResultListUiState
    data class Error(val exception: Exception) : ResultListUiState
}