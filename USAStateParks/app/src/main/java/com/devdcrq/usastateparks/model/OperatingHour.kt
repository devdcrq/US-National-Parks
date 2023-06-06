package com.devdcrq.usastateparks.model

data class OperatingHour(
    val description: String,
    val exceptions: List<Exception>,
    val name: String,
    val standardHours: StandardHours
)