package com.devdcrq.usastateparks.model

import com.devdcrq.usastateparks.util.Equatable
import java.io.Serializable

data class Park(
    val activities: List<Activity>,
    val addresses: List<Address>,
    val contacts: Contacts,
    val description: String,
    val designation: String,
    val directionsInfo: String,
    val directionsUrl: String,
    val entranceFees: List<EntranceFee>,
    val entrancePasses: List<EntrancePass>,
    val fees: List<Any>,
    val fullName: String,
    val id: String,
    val images: List<Image>,
    val latLong: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val operatingHours: List<OperatingHour>,
    val parkCode: String,
    val states: String,
    val topics: List<Topic>,
    val url: String,
    val weatherInfo: String
) : Serializable, Equatable