package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.features.pokemon.models.PokemonListUiModel
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: IPokemonRepository
) : ViewModel() {

    //    private val _uiState: MutableStateFlow<PokemonListUiState> =
//        MutableStateFlow(PokemonListUiState.Loading)
    val uiState = pokemonRepository.getPokemonList(limit = 10, offset = 0).asResultWithLoading()
        .map { result ->
            when (result) {
                Result.Loading -> PokemonListUiState.Loading
                is Result.Success -> PokemonListUiState.Success(result.data.toUiModel())
                is Result.Error -> PokemonListUiState.Error(result.toString())
            }
        }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PokemonListUiState.Loading
    )

}

sealed interface PokemonListUiState {
    object Loading : PokemonListUiState
    data class Success(val data: PokemonListUiModel) : PokemonListUiState
    data class Error(val message: String) : PokemonListUiState
}