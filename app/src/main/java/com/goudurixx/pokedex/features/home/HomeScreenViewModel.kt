package com.goudurixx.pokedex.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    pokemonRepository: IPokemonRepository
) : ViewModel() {

    val uiState: StateFlow<HomeScreenUiState> =
        pokemonRepository.appData.asResultWithLoading().map {
            when (it) {
                is Result.Loading -> {
                    HomeScreenUiState.Loading
                }
                is Result.Success -> {
                    HomeScreenUiState.Success(it.data.totalPokemonCount)
                }
                is Result.Error -> {
                    HomeScreenUiState.Error
                }
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = HomeScreenUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface HomeScreenUiState {
    object Loading : HomeScreenUiState
    data class Success(val pokemonCount: Int) : HomeScreenUiState
    object Error : HomeScreenUiState
}