package com.goudurixx.pokedex.di

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
    fun providePokemonApi(pokemonApi: PokemonApi): IPokemonApi = pokemonApi
    @Provides
    @Singleton
    fun providePokemonRepository(pokemonRepository: PokemonRepository): IPokemonRepository = pokemonRepository

}