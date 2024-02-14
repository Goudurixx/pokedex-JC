package com.goudurixx.pokedex.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.goudurixx.pokedex.core.database.models.PokemonDaoModel
import com.goudurixx.pokedex.data.datasources.PokemonLocalDataSource
import com.goudurixx.pokedex.data.mediator.PokemonRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    fun providePokemonPager(
        remoteMediator: PokemonRemoteMediator,
        localDataSource: PokemonLocalDataSource
    ): Pager<Int, PokemonDaoModel> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { localDataSource.loadAllPokemonsPaged() }
        )
    }
}