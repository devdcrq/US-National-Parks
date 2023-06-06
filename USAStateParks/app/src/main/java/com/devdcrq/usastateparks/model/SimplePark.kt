package com.devdcrq.usastateparks.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devdcrq.usastateparks.util.Equatable
import java.io.Serializable

@Entity(tableName = "parks_table")
data class SimplePark(
    val activities: List<String>,
    val description: String,
    val designation: String,
    @PrimaryKey val id: String,
    val images: List<String>,
    val latitude: String,
    val longitude: String,
    val name: String,
    val states: String,
    val topics: List<String>,
    val url: String,
    var isFavorite: Boolean = false
    ) : Serializable, Equatable