package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.data.models.PokedexGlobalDataModel
import com.goudurixx.pokedex.features.pokemon.models.EvolutionChainUiModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonDetailUiModel
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import com.goudurixx.pokedex.features.pokemon.navigation.PokemonArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val pokemonRepository: IPokemonRepository
) : ViewModel() {

    private val pokemonArgs = PokemonArgs(savedStateHandle)
    private val _pokemonDetail = pokemonRepository.getPokemonDetail(pokemonArgs.id)
    val appDataUiState: StateFlow<AppDataUiState> =
        pokemonRepository.appData.asResultWithLoading().map {
            when (it) {
                is Result.Loading -> AppDataUiState.Loading
                is Result.Success -> {
                    AppDataUiState.Success(it.data)
                }

                is Result.Error -> AppDataUiState.Error(it.exception as Exception)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AppDataUiState.Loading
        )

    private var _speciesUiState = pokemonRepository.getPokemonEvolutionChain(pokemonArgs.id)
    val speciesUiState: StateFlow<PokemonSpeciesUiState> =
        _speciesUiState.asResultWithLoading().map {
            when (it) {
                is Result.Loading -> PokemonSpeciesUiState.Loading
                is Result.Success -> {
                    PokemonSpeciesUiState.Success(it.data.toUiModel())
                }

                is Result.Error -> PokemonSpeciesUiState.Error(it.exception as Exception)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PokemonSpeciesUiState.Loading
        )

    val uiState: StateFlow<PokemonDetailUiState> = _pokemonDetail.asResultWithLoading().map {
        when (it) {
            is Result.Loading -> PokemonDetailUiState.Loading
            is Result.Success -> {
                if(it.data.abilities == null || it.data.stats == null || it.data.types == null){
                    PokemonDetailUiState.FallbackSuccess(it.data.toUiModel(pokemonArgs.color))
                } else {
                    PokemonDetailUiState.Success(it.data.toUiModel(pokemonArgs.color))
                }
            }
            is Result.Error -> PokemonDetailUiState.Error(it.exception as Exception)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PokemonDetailUiState.Loading
    )

    fun updateFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            pokemonRepository.updateFavorite(pokemonArgs.id, isFavorite)
        }
    }
}

sealed interface PokemonDetailUiState {
    object Loading : PokemonDetailUiState

    class Success(val pokemon: PokemonDetailUiModel) : PokemonDetailUiState

    class FallbackSuccess(val pokemon: PokemonDetailUiModel) : PokemonDetailUiState

    class Error(val error: Exception) : PokemonDetailUiState
}

sealed interface PokemonSpeciesUiState {
    object Loading : PokemonSpeciesUiState

    class Success(val species: EvolutionChainUiModel) : PokemonSpeciesUiState

    class Error(val error: Exception) : PokemonSpeciesUiState
}

sealed interface AppDataUiState {
    object Loading : AppDataUiState

    class Success(val appData: PokedexGlobalDataModel) : AppDataUiState

    class Error(val error: Exception) : AppDataUiState
}