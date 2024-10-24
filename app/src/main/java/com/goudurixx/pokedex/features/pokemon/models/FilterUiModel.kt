package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.data.models.PokedexGlobalDataModel


/**
 * Hold the filter of the pokemon list in the UI
 */


abstract class BaseFilterItemUiModel(
    open val type: FilterByParameter,
)

fun defaultBaseFilterItemUiModelList(appDataResult: PokedexGlobalDataModel) =
    listOf(
        RangeFilterItemUiModel(
            FilterByParameter.ID,
            0f..appDataResult.maxId.toFloat(),
            0f..appDataResult.maxId.toFloat()
        ),
        RangeFilterItemUiModel(
            FilterByParameter.BASE_EXPERIENCE,
            appDataResult.minBaseExperience.toFloat()..appDataResult.maxBaseExperience.toFloat(),
            appDataResult.minBaseExperience.toFloat()..appDataResult.maxBaseExperience.toFloat()
        ),
        RangeFilterItemUiModel(
            FilterByParameter.HEIGHT,
            appDataResult.minHeight.toFloat()..appDataResult.maxHeight.toFloat(),
            appDataResult.minHeight.toFloat()..appDataResult.maxHeight.toFloat()
        ),
        RangeFilterItemUiModel(
            FilterByParameter.WEIGHT,
            appDataResult.minWeight.toFloat()..appDataResult.maxWeight.toFloat(),
            appDataResult.minWeight.toFloat()..appDataResult.maxWeight.toFloat()
        ),
        ListFilterUiModel(
            FilterByParameter.TYPE,
            TypeColor.entries.toList().map {
                Pair(TypeUiModel(id = it.id, it.name, it.color), false)
            }
        ),
        ListFilterUiModel(
            FilterByParameter.GENERATION,
            appDataResult.generationList.map {
                Pair(it.toUiModel(), false)
            }
        ),
        BooleanFilterUiModel(
            FilterByParameter.IS_LEGENDARY,
            null
        ),
        BooleanFilterUiModel(
            FilterByParameter.IS_DEFAULT,
            null
        ),
        BooleanFilterUiModel(
            FilterByParameter.IS_BABY,
            null
        ),
        BooleanFilterUiModel(
            FilterByParameter.IS_MYTHICAL,
            null
        ),
    )


fun List<BaseFilterItemUiModel>.updateFilterList(
    index: Int,
    filterItem: BaseFilterItemUiModel
): List<BaseFilterItemUiModel> {
    val filterListCopy = this.toMutableList()
    filterListCopy[index] = filterItem
    return filterListCopy
}

data class RangeFilterItemUiModel(
    override val type: FilterByParameter,
    var value: ClosedFloatingPointRange<Float>,
    val range: ClosedFloatingPointRange<Float>,
    val steps: Int = 0
) : BaseFilterItemUiModel(type)

data class ListFilterUiModel(
    override val type: FilterByParameter,
    var list: List<Pair<ListFilterItemUiModel, Boolean>>
) : BaseFilterItemUiModel(type) {
    fun updateList(index: Int, value: Boolean): ListFilterUiModel {
        val listCopy = list.toMutableList()
        listCopy[index] = list[index].copy(second = !value)
        return ListFilterUiModel(type, listCopy)
    }
}

abstract class ListFilterItemUiModel{
   abstract val id: Int
}

data class BooleanFilterUiModel(
    override val type: FilterByParameter,
    var value: Boolean?
) : BaseFilterItemUiModel(type)