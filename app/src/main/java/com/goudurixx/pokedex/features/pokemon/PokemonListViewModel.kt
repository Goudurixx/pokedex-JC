package com.goudurixx.pokedex.features.pokemon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.core.utils.asResult
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.data.models.PokedexGlobalDataModel
import com.goudurixx.pokedex.domain.mappers.FilterMapper
import com.goudurixx.pokedex.features.pokemon.models.BaseFilterItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem
import com.goudurixx.pokedex.features.pokemon.models.defaultBaseFilterItemUiModelList
import com.goudurixx.pokedex.features.pokemon.models.sortOrderItemList
import com.goudurixx.pokedex.features.pokemon.models.toOrderBy
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: IPokemonRepository,
) : ViewModel() {

    private val _sortFilterList = MutableStateFlow(sortOrderItemList())
    private val _appDataResult = MutableStateFlow<PokedexGlobalDataModel?>(null)
    val sortFilterList = _sortFilterList.asStateFlow()

    private var _filterList: MutableStateFlow<List<BaseFilterItemUiModel>> =
        MutableStateFlow(emptyList())

    val filterList: StateFlow<List<BaseFilterItemUiModel>> =
        _filterList.combine(pokemonRepository.appData.asResultWithLoading()) { filterList, appDataResult ->
            when (appDataResult) {
                Result.Loading -> filterList
                is Result.Success -> {
                    filterList.ifEmpty {
                        _appDataResult.value = appDataResult.data
                        defaultBaseFilterItemUiModelList(appDataResult.data)
                    }
                }

                is Result.Error -> filterList
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

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

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pokemonPagingFlow =
        combine(_sortFilterList, _filterList) { sort, filter -> Pair(sort, filter) }.debounce(400)
            .flatMapLatest { filterPair ->
                pokemonRepository.getPokemonPagerList(
                    filterPair.first.firstOrNull { sortOrderItem -> sortOrderItem.order != null }
                        ?.toOrderBy(),
                    filterPair.second.map { FilterMapper().mapToFilterBy(it) })
                    .map { pagingData ->
                        pagingData.map { it.toUiModel() }
                    }
            }
            .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            pokemonRepository.appData.asResultWithLoading().map {
                when (it) {
                    Result.Loading -> {
                        AppDataUiState.Loading
                    }
                    is Result.Success -> {
                        _filterList = MutableStateFlow(
                            listOf<BaseFilterItemUiModel>()
                        )
                        AppDataUiState.Success(it.data)
                    }

                    is Result.Error -> {
                        AppDataUiState.Error(it.exception as Exception)
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = AppDataUiState.Loading
            )
        }
    }

    fun updateSearch(search: String) {
        _search.update { search }
    }

    fun updateSort(sortOrderItem: SortOrderItem) {
        _sortFilterList.update { _ ->
            sortOrderItemList(sortOrderItem)
        }
    }

    fun updateFilter(filterList: List<BaseFilterItemUiModel>) {
        _filterList.update { _ ->
            filterList
        }
    }

    fun resetFilter() {
        _filterList.update { _ ->
            _appDataResult.value?.let { defaultBaseFilterItemUiModelList(appDataResult = it) } ?: emptyList()
        }
    }

    fun updateFavorite(pokemonId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            pokemonRepository.updateFavorite(pokemonId, isFavorite).asResult().collect{result ->
                Log.e("PokemonListViewModel", "updateFavorite: $result")
            }
        }
    }
}

sealed interface SearchUiState {
    object None : SearchUiState

    object Loading : SearchUiState

    data class Success(val list: List<PokemonListItemUiModel>) : SearchUiState

    object Error : SearchUiState
}