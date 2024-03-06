package com.goudurixx.pokedex

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
class MainActivityViewModel @Inject constructor(
    pokemonRepository: IPokemonRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> =
        pokemonRepository.appData.asResultWithLoading().map {
            when (it) {
                is Result.Loading -> {
                    MainActivityUiState.Loading
                }
                is Result.Success -> {
                    MainActivityUiState.Success(it.data.lastUpdated.toString() + " " + it.data.totalPokemonCount)
                }
                is Result.Error -> {
                    MainActivityUiState.Error
                }
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = MainActivityUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val data: String) : MainActivityUiState
    object Error : MainActivityUiState
}
