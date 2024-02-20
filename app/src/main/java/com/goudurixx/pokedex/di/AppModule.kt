package com.goudurixx.pokedex.di

import com.apollographql.apollo3.ApolloClient
import com.goudurixx.pokedex.core.network.IPokemonApi
import com.goudurixx.pokedex.core.network.services.PokemonApi
import com.goudurixx.pokedex.data.IPokemonRepository
import com.goudurixx.pokedex.data.repositories.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://beta.pokeapi.co/graphql/v1beta")
            .build()
    }


    @Provides
    @Singleton
    fun providePokemonApi(pokemonApi: PokemonApi): IPokemonApi {
        return pokemonApi
    }
    @Provides
    @Singleton
    fun providePokemonRepository(pokemonRepository: PokemonRepository): IPokemonRepository = pokemonRepository

}