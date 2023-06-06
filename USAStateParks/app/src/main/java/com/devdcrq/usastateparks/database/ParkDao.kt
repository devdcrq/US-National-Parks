package com.devdcrq.usastateparks.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devdcrq.usastateparks.model.SimplePark

@Dao
interface ParkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFavoritePark(vararg simplePark: SimplePark): List<Long>

    @Query("SELECT * FROM parks_table")
    fun getAllParksFromDb(): LiveData<List<SimplePark>>

    @Query("SELECT * FROM parks_table WHERE isFavorite = 1")
    fun getAllFavoriteParks(): LiveData<List<SimplePark>>

    @Delete
    suspend fun deleteFavoritePark(simplePark: SimplePark)
}