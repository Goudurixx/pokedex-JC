package com.goudurixx.pokedex.features.pokemon.models

import com.goudurixx.pokedex.core.common.models.OrderBy
import com.goudurixx.pokedex.core.common.models.OrderByParameter
import com.goudurixx.pokedex.core.common.models.OrderByValues


/**
 * Hold the sort order of the pokemon list in the UI
 * @param parameter the parameter to sort by
 * @param order the order to sort by being null means no sorting along this parameter
 */
data class SortOrderItem(
    val parameter: OrderByParameter,
    var order: OrderByValues? = null
)

fun sortOrderItemList(item: SortOrderItem? = null): List<SortOrderItem> {
    return OrderByParameter.entries.map { orderByParameter ->
        SortOrderItem(
            orderByParameter,
            item?.let { if (orderByParameter == item.parameter) item.order else null })
    }
}

fun SortOrderItem.toOrderBy(): OrderBy? {
    return order?.let { order -> OrderBy(parameter, order) }
}