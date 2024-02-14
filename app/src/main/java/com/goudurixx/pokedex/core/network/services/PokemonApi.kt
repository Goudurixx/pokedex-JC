package com.goudurixx.pokedex.core.network.services

import com.goudurixx.pokedex.core.network.IPokemonApi
import com.goudurixx.pokedex.core.network.models.PokemonListResponse
import com.goudurixx.pokedex.core.network.models.PokemonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import javax.inject.Inject

class PokemonApi @Inject constructor() : IPokemonApi {
    private val client = HttpClient {
        default()
        defaultRequest {
            url("https://pokeapi.co/api/v2/")
            header("Accept", "*/*")
        }
    }

    override suspend fun getPokemonList(limit : Int, offset : Int) : PokemonListResponse =
        client.get("pokemon?limit=$limit&offset=$offset").body()


    override suspend fun getPokemonDetail(name: String): PokemonResponse =
        client.get("pokemon/$name").body()


}