package com.goudurixx.pokedex.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.features.pokemon.SearchUiState
import com.goudurixx.pokedex.features.pokemon.models.GenerationUiModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val pokemonRepository: IPokemonRepository
) : ViewModel() {

    private val _search: MutableStateFlow<String> = MutableStateFlow("")
    val search = _search.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchState = _search.debounce(400).filter { it.length > 1 }.flatMapLatest {
        pokemonRepository.getPokemonCompletion(it).asResultWithLoading()
    }.map { result ->
        when (result) {
            Result.Loading -> SearchUiState.Loading
            is Result.Error -> SearchUiState.Error
            is Result.Success -> SearchUiState.Success(result.data.mapIndexed { index, pokemon ->
                pokemon.toUiModel(
                    indexIn = index
                )
            })
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchUiState.None
    )

    val uiState: StateFlow<HomeScreenUiState> =
        pokemonRepository.appData.asResultWithLoading().map {
    private val _uiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()


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

    init {
        viewModelScope.launch {
            pokemonRepository.appData.asResultWithLoading().collectLatest { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { HomeScreenUiState.Loading }
                    is Result.Success ->
                        _uiState.update {
                            HomeScreenUiState.Success(
                                result.data.totalPokemonCount,
                                result.data.lastUpdated,
                                result.data.generationList.map { it.toUiModel() }
                            )
                        }
                    is Result.Error -> _uiState.update { HomeScreenUiState.Error }
                }
            }
        }
    }

    fun updateSearch(search: String) {
        _search.update { search }
    }

    fun updateAppData() {
        viewModelScope.launch {
            pokemonRepository.updateAppData().asResultWithLoading().collectLatest { result ->
                Log.e("HomeScreenViewModel", "updateAppData: $result")
                when (result) {
                    is Result.Loading -> {
                        if(_uiState.value is HomeScreenUiState.Success){
                            val pokemonCount = (_uiState.value as HomeScreenUiState.Success).pokemonCount
                            val lastUpdated = (_uiState.value as HomeScreenUiState.Success).lastUpdated
                            val generationList = (_uiState.value as HomeScreenUiState.Success).generationList
                        _uiState.update { HomeScreenUiState.SuccessReloading(pokemonCount, lastUpdated, generationList) }
                        }
                    }
                    is Result.Success ->
                        _uiState.update {
                            HomeScreenUiState.Success(
                                result.data.totalPokemonCount,
                                result.data.lastUpdated,
                                result.data.generationList.map { it.toUiModel() }
                            )
                        }
                    is Result.Error ->{
                        if(_uiState.value is HomeScreenUiState.Success){
                            val pokemonCount = (_uiState.value as HomeScreenUiState.Success).pokemonCount
                            val lastUpdated = (_uiState.value as HomeScreenUiState.Success).lastUpdated
                            val generationList = (_uiState.value as HomeScreenUiState.Success).generationList
                            _uiState.update { HomeScreenUiState.Success(pokemonCount, lastUpdated, generationList) }
                        } else if (_uiState.value is HomeScreenUiState.SuccessReloading) {
                            val pokemonCount = (_uiState.value as HomeScreenUiState.Success).pokemonCount
                            val lastUpdated = (_uiState.value as HomeScreenUiState.Success).lastUpdated
                            val generationList = (_uiState.value as HomeScreenUiState.Success).generationList
                            _uiState.update { HomeScreenUiState.Success(pokemonCount, lastUpdated, generationList) }
                        }
                    }
                }
            }
        }
    }
}

sealed interface HomeScreenUiState {
    object Loading : HomeScreenUiState
    data class Success(val pokemonCount: Int) : HomeScreenUiState
    data class Success(val pokemonCount: Int, val lastUpdated: Long,val generationList : List<GenerationUiModel>) : HomeScreenUiState
    object Error : HomeScreenUiState
}