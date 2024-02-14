package com.goudurixx.pokedex.features.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.features.pokemon.models.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    pager: Pager<Int, PokemonDaoModel>
) : ViewModel() {

    val pokemonPagingFlow = pager.flow.map { pagingData ->
        pagingData.map { it.toUiModel() }
    }.cachedIn(viewModelScope)

}