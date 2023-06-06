package com.devdcrq.usastateparks.repository

import com.devdcrq.usastateparks.api.RetrofitInstance
import com.devdcrq.usastateparks.database.ParksDatabase
import com.devdcrq.usastateparks.model.SimplePark

class ParksRepository(val db: ParksDatabase) {

    suspend fun getAllParks(stateCode: String) = RetrofitInstance.api.getAllParks(stateCode)

    suspend fun searchParks(searchQuery: String) = RetrofitInstance.api.searchParks(searchQuery)

    suspend fun upsertFavoritePark(vararg simplePark: SimplePark) =
        db.getParkDao().upsertFavoritePark(*simplePark)

    fun getAllParksFromDb() = db.getParkDao().getAllParksFromDb()

    fun getAllFavoriteParks() = db.getParkDao().getAllFavoriteParks()

    suspend fun deleteFavoritePark(simplePark: SimplePark) =
        db.getParkDao().deleteFavoritePark(simplePark)
}