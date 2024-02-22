package com.goudurixx.pokedex.core.common.models

enum class OrderByValues {
    ASC,
    DESC
}

enum class OrderByParameter(val parameterName: String) {
    ID("id"),
    NAME("name"),
    HEIGHT("height"),
    WEIGHT("weight"),
    BASE_EXPERIENCE("base_experience")
}

data class OrderBy(val parameter: OrderByParameter, val value: OrderByValues)
