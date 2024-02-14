package com.goudurixx.pokedex.data.datasources

import com.goudurixx.pokedex.core.database.daos.PokemonDao
import javax.inject.Inject

class PokemonLocalDataSource @Inject constructor(
    private val pokemonDao: PokemonDao
){
    fun getAllPokemons() = pokemonDao.getAllPokemons()
}