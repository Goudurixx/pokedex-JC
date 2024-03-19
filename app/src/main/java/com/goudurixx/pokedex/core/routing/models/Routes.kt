package com.goudurixx.pokedex.core.routing.models

/**
 * List of all features roots routes
 */
enum class Routes(val route: String) {
    HOME(route = "home"),
    POKEMON(route = "pokemon_list"),
    TYPE(route = "pokemon_type"),
    FAVORITE(route = "favorite"),
    POKEMAP(route = "pokemap"),
}