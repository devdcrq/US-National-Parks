package com.devdcrq.usastateparks.database

import androidx.room.TypeConverter
import com.devdcrq.usastateparks.model.Park
import com.devdcrq.usastateparks.model.SimplePark
import java.lang.StringBuilder
import java.util.regex.Pattern

object Converters {

    fun convertParkToSimplePark(park: Park): SimplePark {
        val activities = mutableListOf<String>()
        park.activities.forEach { activity -> activities.add(activity.toString()) }
        val description = park.description
        val designation = park.designation
        val id = park.id
        val images = mutableListOf<String>()
        park.images.forEach { image -> images.add(image.toString()) }
        val latitude = park.latitude
        val longitude = park.longitude
        val name = park.name
        val states = park.states
        val topics = mutableListOf<String>()
        park.topics.forEach { topic -> topics.add(topic.toString()) }
        val url = park.url
        return SimplePark(
            activities, description, designation, id, images,
            latitude, longitude, name, states, topics, url
        )
    }

    @TypeConverter
    fun fromActivities(list: List<String>): String {
        val sb = StringBuilder()
        list.forEach { sb.append(it).append(", ") }
        return sb.toString().dropLast(2)
    }

    @TypeConverter
    fun toActivities(s: String): List<String> {
        val pattern = Pattern.compile(", ")
        return s.split(pattern, 0)
    }

}