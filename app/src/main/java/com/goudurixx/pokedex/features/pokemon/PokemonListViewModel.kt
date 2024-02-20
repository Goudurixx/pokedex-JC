package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.data.models.PokemonListItemModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    pokemonRepository: IPokemonRepository,
    pager: Pager<Int, PokemonDaoModel>
) : ViewModel() {

    private val _search: MutableStateFlow<String> = MutableStateFlow("")
    val search = _search.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchState = _search.debounce(400).filter { it.length > 3 }.flatMapLatest {
        pokemonRepository.getPokemonCompletion(it).asResultWithLoading()
    }.map { result ->
        when (result) {
            Result.Loading -> SearchUiState.Loading
            is Result.Error -> SearchUiState.Error
            is Result.Success -> SearchUiState.Success(result.data.map { it.toUiModel() })
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchUiState.None
    )

    val pokemonPagingFlow = pager.flow.map { pagingData ->
        pagingData.map { it.toUiModel() }
    }.cachedIn(viewModelScope)


    fun updateSearch(search: String) {
        _search.update { search }
    }
}

sealed interface SearchUiState {
    object None : SearchUiState

    object Loading : SearchUiState

    data class Success(val list: List<PokemonListItemUiModel>) : SearchUiState

    object Error : SearchUiState
}