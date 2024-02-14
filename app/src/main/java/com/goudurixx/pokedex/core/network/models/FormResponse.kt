package com.goudurixx.pokedex.core.network.models

import kotlinx.serialization.Serializable

@Serializable
data class FormResponse(
    val name: String,
    val url: String
)