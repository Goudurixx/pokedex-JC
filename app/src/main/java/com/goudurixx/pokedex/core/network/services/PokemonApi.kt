package com.goudurixx.pokedex.core.network.services

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.goudurixx.pokedex.PokemonEvolutionChainQuery
import com.goudurixx.pokedex.PokemonSearchCompletionQuery
import com.goudurixx.pokedex.core.network.IPokemonApi
import com.goudurixx.pokedex.core.network.models.EvolutionChainResponse
import com.goudurixx.pokedex.core.network.models.PokemonListResponse
import com.goudurixx.pokedex.core.network.models.PokemonResponse
import com.goudurixx.pokedex.core.network.models.toResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import javax.inject.Inject

class PokemonApi @Inject constructor(
    private val apolloClient: ApolloClient
) : IPokemonApi {
    private val client = HttpClient {
        default()
        defaultRequest {
            url("https://pokeapi.co/api/v2/")
            header("Accept", "*/*")
        }
    }

    override suspend fun getPokemonList(limit: Int, offset: Int): PokemonListResponse =
        client.get("pokemon?limit=$limit&offset=$offset").body()

    override suspend fun getPokemonSearchCompletion(query: String): PokemonListResponse {
        return apolloClient.query(PokemonSearchCompletionQuery(query = Optional.present(query)))
            .execute()
            .data
            ?.toResponseModel() ?: PokemonListResponse(results = emptyList())
    }

    override suspend fun getPokemonDetail(id: Int): PokemonResponse =
        client.get("pokemon/$id").body()

}