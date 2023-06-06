package com.devdcrq.usastateparks.model

data class Address(
    val city: String,
    val countryCode: String,
    val line1: String,
    val line2: String,
    val line3: String,
    val postalCode: String,
    val provinceTerritoryCode: String,
    val stateCode: String,
    val type: String
)