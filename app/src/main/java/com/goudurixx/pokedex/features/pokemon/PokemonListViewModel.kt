package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.utils.Result
import com.goudurixx.pokedex.core.utils.asResultWithLoading
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.domain.mappers.FilterMapper
import com.goudurixx.pokedex.features.pokemon.models.BaseFilterItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.BooleanFilterUiModel
import com.goudurixx.pokedex.features.pokemon.models.PokemonListItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.RangeFilterItemUiModel
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
import kotlinx.coroutines.flow.combine
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

    private val _filterList = MutableStateFlow(
        listOf<BaseFilterItemUiModel>(
            RangeFilterItemUiModel(FilterByParameter.ID, 0f..1000f, 0f..1000f),
            RangeFilterItemUiModel(FilterByParameter.BASE_EXPERIENCE, 0f..400f, 0f..400f),
//            RangeFilterItemUiModel(FilterByParameter.HP, 0f..255f, 0f..255f),
//            RangeFilterItemUiModel(FilterByParameter.ATTACK, 0f..255f, 0f..255f),
            RangeFilterItemUiModel(FilterByParameter.HEIGHT, 0f..10000f, 0f..10000F),
            RangeFilterItemUiModel(FilterByParameter.WEIGHT, 0f..10000f, 0f..10000F),
//            BooleanFilterUiModel(FilterByParameter.IS_DEFAULT, false),
//            ListFilterUiModel<TypeUiModel>(
//                FilterByParameter.TYPE,
//                listOf(
//                    Pair(TypeUiModel(id = 1, "fire", TypeFire), false),
//                    Pair(TypeUiModel(id = 2, "water", TypeWater), false)
//                )
//            ),
            BooleanFilterUiModel(FilterByParameter.IS_LEGENDARY, null), //TODO CHECK THE THIRD STATE
            BooleanFilterUiModel(FilterByParameter.IS_DEFAULT, null), //TODO CHECK THE THIRD STATE
        )
    ) //TODO MOVE DEPENDENCY OUT OF VIEWMODEL

    val filterList = _filterList.asStateFlow()

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
}

sealed interface SearchUiState {
    object None : SearchUiState

    object Loading : SearchUiState

    data class Success(val list: List<PokemonListItemUiModel>) : SearchUiState

    object Error : SearchUiState
}