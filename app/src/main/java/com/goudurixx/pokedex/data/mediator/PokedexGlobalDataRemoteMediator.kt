package com.goudurixx.pokedex.data.mediator

import android.util.Log
import androidx.room.withTransaction
import com.goudurixx.pokedex.core.database.PokedexDatabase
import com.goudurixx.pokedex.core.database.models.PokedexGlobalDataDaoModel
import com.goudurixx.pokedex.core.database.models.toDaoModel
import com.goudurixx.pokedex.data.datasources.PokemonRemoteDataSource
import com.goudurixx.pokedex.data.models.PokedexGlobalDataModel
import com.goudurixx.pokedex.data.models.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokedexGlobalDataRemoteMediator @Inject constructor(
    private val remoteDataSource: PokemonRemoteDataSource,
    private val pokedexDatabase: PokedexDatabase
) {
    fun load(): Flow<PokedexGlobalDataModel> {
       return flow{
            try {
                Log.e("PokedexGlobalDataMediator", "Tryng to load data")
                /** check if i need to fetch new data :
                 * - I do if there is no data in the database
                 * - I do if the last update is older than 24 hours in this case
                 *       -> check the total count vs the remote totalCount
                 *       -> if the remote count is different, fetch the new data
                 *       -> if the remote count is the same, update the lastUpdated field
                 * DO NOT APPLY THIS LOGIC TOTALLY YET TODO
                 */
                val localData: PokedexGlobalDataDaoModel? = try {
                    pokedexDatabase.pokedexGlobalDataDao().get()
                } catch (e: Exception) {
                    Log.e("PokedexGlobalDataMediator", "An error occured while trying to fetch info from DB", e)
                    null
                }

                if (localData == null || localData.lastUpdated < System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
                    val remoteData = remoteDataSource.getPokedexGlobalData().toDataModel()
                    pokedexDatabase.withTransaction {
                        pokedexDatabase.pokedexGlobalDataDao().insert(remoteData.toDaoModel())
                    }
                }

              emit(pokedexDatabase.pokedexGlobalDataDao().get().toDataModel())
            } catch (e: Exception) {
                Log.e(
                    "PokedexGlobalDataMediator",
                    "An error occured while trying to fetch up to date info",
                    e
                )
                error(e)
            }
        }
    }
}