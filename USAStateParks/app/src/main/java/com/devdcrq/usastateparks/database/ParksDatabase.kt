package com.devdcrq.usastateparks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devdcrq.usastateparks.model.SimplePark

@Database(entities = [SimplePark::class], version = 1)
@TypeConverters(Converters::class)
abstract class ParksDatabase : RoomDatabase() {

    abstract fun getParkDao(): ParkDao

    companion object {

        @Volatile private var instance: ParksDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ParksDatabase::class.java,
            "parks_database")
                .build()
    }
}