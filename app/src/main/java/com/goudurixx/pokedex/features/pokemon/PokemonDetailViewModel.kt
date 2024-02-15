package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.features.pokemon.navigation.PokemonArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.features.pokemon.models.PokemonDetailUiModel
import com.goudurixx.pokedex.features.pokemon.models.toUiModel

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    pokemonRepository: IPokemonRepository
) : ViewModel(){

    private val pokemonArgs = PokemonArgs(savedStateHandle)

    val uiState : StateFlow<PokemonDetailUiState> = pokemonRepository.getPokemonDetail(pokemonArgs.id).asResultWithLoading().map {
        when(it){
            is Result.Loading -> PokemonDetailUiState.Loading
            is Result.Success -> PokemonDetailUiState.Success(it.data.toUiModel())
            is Result.Error -> PokemonDetailUiState.Error
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PokemonDetailUiState.Loading
    )
}

sealed interface PokemonDetailUiState {
    object Loading : PokemonDetailUiState

    class Success(val pokemon: PokemonDetailUiModel) : PokemonDetailUiState

    object Error : PokemonDetailUiState
}