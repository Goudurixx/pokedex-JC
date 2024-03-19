package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.common.models.ListFilterValue
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.data.models.TypeModel
import com.goudurixx.pokedex.features.pokemon.models.TypeUiModel
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import com.goudurixx.pokedex.features.pokemon.navigation.TypeArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonTypeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pokemonRepository: IPokemonRepository,
) : ViewModel() {

    private val typeArgs = TypeArgs(savedStateHandle)

    private val _uiState: MutableStateFlow<TypeUiState> =
        MutableStateFlow(TypeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val pokemonPagingFlow =
        pokemonRepository.getPokemonPagerList(
            null,
            listOf<FilterBy>(
                FilterBy(
                    parameter = FilterByParameter.TYPE,
                    value = ListFilterValue(value = listOf(typeArgs.typeId))
                )
            )
        ).map { pagingData ->
            pagingData.map { it.toUiModel() }
        }.cachedIn(viewModelScope)

    init {
        getTypeInfo()
    }

    private fun getTypeInfo() {
        viewModelScope.launch {
            _uiState.update {
                TypeUiState.Success(TypeModel(typeArgs.typeId, typeArgs.typeName).toUiModel())
            }
        }
    }
}


sealed interface TypeUiState {
    object Loading : TypeUiState
    data class Success(val data: TypeUiModel) : TypeUiState
    data class Error(val exception: Exception) : TypeUiState
}