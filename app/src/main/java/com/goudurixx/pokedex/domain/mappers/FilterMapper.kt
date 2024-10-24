package com.goudurixx.pokedex.domain.mappers


import com.goudurixx.pokedex.core.common.models.BooleanFilterValue
import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.IntRangeFilterValue
import com.goudurixx.pokedex.core.common.models.ListFilterValue
import com.goudurixx.pokedex.features.pokemon.models.BaseFilterItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.BooleanFilterUiModel
import com.goudurixx.pokedex.features.pokemon.models.ListFilterUiModel
import com.goudurixx.pokedex.features.pokemon.models.RangeFilterItemUiModel

class FilterMapper {

    fun mapToFilterBy(filterUiModel: BaseFilterItemUiModel): FilterBy {
        return when (filterUiModel) {
            is BooleanFilterUiModel -> FilterBy(filterUiModel.type, BooleanFilterValue(filterUiModel.value))
            is RangeFilterItemUiModel -> FilterBy(filterUiModel.type, IntRangeFilterValue(filterUiModel.value.start.toInt()..filterUiModel.value.endInclusive.toInt()))
            is ListFilterUiModel -> FilterBy(filterUiModel.type, ListFilterValue(filterUiModel.list.filter { it.second }.map { it.first.id }, filterUiModel.type))
            else -> throw IllegalArgumentException("Unsupported filter type")
        }
    }

    fun navigation(filterUiModel: BaseFilterItemUiModel): FilterBy {
        return when (filterUiModel) {
            is BooleanFilterUiModel -> FilterBy(filterUiModel.type, BooleanFilterValue(filterUiModel.value))
            is RangeFilterItemUiModel -> FilterBy(filterUiModel.type, IntRangeFilterValue(filterUiModel.value.start.toInt()..filterUiModel.value.endInclusive.toInt()))
            is ListFilterUiModel -> FilterBy(filterUiModel.type, ListFilterValue(filterUiModel.list.filter { it.second }.map { it.first }, filterUiModel.type))
            else -> throw IllegalArgumentException("Unsupported filter type")
        }
    }
}