package com.goudurixx.pokedex.core.network.services

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.goudurixx.pokedex.PokemonEvolutionChainQuery
import com.goudurixx.pokedex.PokemonGetPagedListQuery
import com.goudurixx.pokedex.PokemonSearchCompletionQuery
import com.goudurixx.pokedex.core.network.IPokemonApi
import com.goudurixx.pokedex.core.network.models.EvolutionChainResponse
import com.goudurixx.pokedex.core.network.models.OrderByParametersNetworkModel
import com.goudurixx.pokedex.core.network.models.PokemonListResponse
import com.goudurixx.pokedex.core.network.models.PokemonResponse
import com.goudurixx.pokedex.core.network.models.WhereParametersNetworkModel
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

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int,
        orderByParameters: OrderByParametersNetworkModel?,
        whereParameters: WhereParametersNetworkModel?
    ): PokemonListResponse = apolloClient.query(
        PokemonGetPagedListQuery(
            _limit = Optional.present(limit),
            _offset = Optional.present(offset),
            _order_by = Optional.presentIfNotNull(orderByParameters?.parameters),
            _where = Optional.presentIfNotNull(whereParameters?.parameters)
        )
    )
        .execute()
        .data
        ?.toResponseModel() ?: PokemonListResponse(results = emptyList())

    override suspend fun getPokemonSearchCompletion(query: String): PokemonListResponse =
        apolloClient.query(PokemonSearchCompletionQuery(query = Optional.present(query)))
            .execute()
            .data
            ?.toResponseModel() ?: PokemonListResponse(results = emptyList())

    override suspend fun getPokemonDetail(id: Int): PokemonResponse =
        client.get("pokemon/$id").body()

    override suspend fun getPokemonEvolutionChain(id: Int): EvolutionChainResponse {
        return apolloClient.query(PokemonEvolutionChainQuery(_id = Optional.present(id)))
            .execute()
            .data?.pokemon_v2_pokemon_by_pk?.pokemon_v2_pokemonspecy?.pokemon_v2_evolutionchain?.toResponseModel()
            ?: EvolutionChainResponse(
                id = id,
                pokemon_v2_pokemonspecies = emptyList()
            )
    }

}