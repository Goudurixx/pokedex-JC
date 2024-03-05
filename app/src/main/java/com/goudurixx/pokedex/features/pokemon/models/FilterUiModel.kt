package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.core.common.models.FilterByParameter


/**
 * Hold the filter of the pokemon list in the UI
 */


abstract class BaseFilterItemUiModel(
    open val type : FilterByParameter,
)

fun List<BaseFilterItemUiModel>.updateFilterList(index : Int, filterItem: BaseFilterItemUiModel) : List<BaseFilterItemUiModel>{
    val filterListCopy = this.toMutableList()
    filterListCopy[index] = filterItem
    return filterListCopy
}

data class RangeFilterItemUiModel(
    override val type: FilterByParameter,
    var value:  ClosedFloatingPointRange<Float>,
    val range: ClosedFloatingPointRange<Float>,
    val steps : Int = 0
) : BaseFilterItemUiModel(type)

data class ListFilterUiModel<T>(
    override val type: FilterByParameter,
    var list: List<Pair<T, Boolean>>
) : BaseFilterItemUiModel(type) {
    fun updateList(index: Int, value: Boolean) : ListFilterUiModel<T> {
        val listCopy = list.toMutableList()
        listCopy[index] = list[index].copy(second = value)
       return ListFilterUiModel(type, listCopy)
    }
}

data class BooleanFilterUiModel(
    override val type: FilterByParameter,
    var value: Boolean?
) : BaseFilterItemUiModel(type)