package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem
import com.goudurixx.pokedex.features.pokemon.models.sortOrderItemList
import com.goudurixx.pokedex.features.pokemon.models.toOrderBy
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: IPokemonRepository,
) : ViewModel() {

    private val _sortFilterList = MutableStateFlow(sortOrderItemList())
    val sortFilterList = _sortFilterList.asStateFlow()

    private val _search: MutableStateFlow<String> = MutableStateFlow("")
    val search = _search.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchState = _search.debounce(400).filter { it.length > 3 }.flatMapLatest {
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

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pokemonPagingFlow = _sortFilterList.debounce(400).flatMapLatest { sortOrderList ->
        pokemonRepository.getPokemonPagerList(sortOrderList.firstOrNull { sortOrderItem -> sortOrderItem.order != null }
            ?.toOrderBy())
            .map { pagingData ->
                pagingData.map { it.toUiModel() }
            }
    }
        .cachedIn(viewModelScope)

    fun updateSearch(search: String) {
        _search.update { search }
    }

    fun updateSort(sortOrderItem: SortOrderItem) {
        _sortFilterList.update { _ ->
            sortOrderItemList(sortOrderItem)
        }
    }
}

sealed interface SearchUiState {
    object None : SearchUiState

    object Loading : SearchUiState

    data class Success(val list: List<PokemonListItemUiModel>) : SearchUiState

    object Error : SearchUiState
}