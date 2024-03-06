package com.goudurixx.pokedex.core.database.di

import android.content.Context
import androidx.room.Room
import com.goudurixx.pokedex.core.database.PokedexDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesPokedexDatabase(
        @ApplicationContext context: Context,
    ): PokedexDatabase = Room.databaseBuilder(
        context,
        PokedexDatabase::class.java,
        "pokedex-database",
    ).build()


}