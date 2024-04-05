package com.goudurixx.pokedex.core.common.models

import com.apollographql.apollo3.api.Optional
import com.goudurixx.pokedex.type.Boolean_comparison_exp
import com.goudurixx.pokedex.type.Int_comparison_exp

enum class FilterByParameter(val parameterName: String) {
    ID("id"),
    HEIGHT("height"),
    WEIGHT("weight"),
    BASE_EXPERIENCE("base_experience"),
    HP("hp"),
    DEFENSE("defense"),
    ATTACK("attack"),
    TYPE("type"),
    GENERATION("generation"),
    IS_LEGENDARY("is_legendary"),
    IS_DEFAULT("is_default"),
    IS_BABY("is_baby"),
    IS_MYTHICAL("is_mythical"),
}

data class FilterBy(val parameter: FilterByParameter, val value: BaseFilterValue)

abstract class BaseFilterValue {
    abstract fun toFilterNetworkModel(): Any
}


data class BooleanFilterValue(val value: Boolean?) : BaseFilterValue() {
    override fun toFilterNetworkModel() =
        Optional.present(Boolean_comparison_exp(_eq = Optional.presentIfNotNull(value)))
}

data class IntRangeFilterValue(val value: IntRange) : BaseFilterValue() {
    override fun toFilterNetworkModel(): Optional<Int_comparison_exp> {
        return Optional.present(
            Int_comparison_exp(
                _gte = Optional.present(value.first),
                _lte = Optional.present(value.last)
            )
        )
    }
}

data class ListFilterValue<T>(
    var value: List<T>,
    val type: FilterByParameter
) : BaseFilterValue() {
    override fun toFilterNetworkModel() =
            when (type) {
                FilterByParameter.TYPE -> if(value.isNotEmpty())Optional.present(Int_comparison_exp(_in = Optional.present((this.value as List<Int>)))) else Optional.Absent
                FilterByParameter.GENERATION ->if(value.isNotEmpty()) Optional.present(Int_comparison_exp(_in = Optional.present((this.value as List<Int>)))) else Optional.Absent
                else -> throw IllegalArgumentException("Type not supported, value is of type ${this::class.simpleName}")
            }
}