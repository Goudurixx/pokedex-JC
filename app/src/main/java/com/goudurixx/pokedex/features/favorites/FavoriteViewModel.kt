package com.goudurixx.pokedex.features.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.features.pokemon.SearchUiState
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val pokemonRepository: IPokemonRepository,
) : ViewModel() {

    val uiState =
        pokemonRepository.getAllFavoritePokemon().asResultWithLoading().map { result ->
            when (result) {
                Result.Loading -> FavoriteUiState.Loading
                is Result.Error -> FavoriteUiState.Error
                is Result.Success -> FavoriteUiState.Success(result.data.mapIndexed { index, pokemon ->
                    pokemon.toUiModel(
                        indexIn = index
                    )
                })
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = FavoriteUiState.Loading
        )

    fun updateFavorite(pokemonId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            Log.e("PokemonListViewModel", "updateFavorite: $pokemonId, $isFavorite")
            pokemonRepository.updateFavorite(pokemonId, isFavorite)
        }
    }
}

sealed interface FavoriteUiState {

    object Loading : FavoriteUiState

    data class Success(val list: List<PokemonListItemUiModel>) : FavoriteUiState

    object Error : FavoriteUiState
}