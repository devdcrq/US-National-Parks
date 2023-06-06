package com.devdcrq.usastateparks.model

data class ParksResponse(
    val data: List<Park>,
    val limit: String,
    val start: String,
    val total: String
)