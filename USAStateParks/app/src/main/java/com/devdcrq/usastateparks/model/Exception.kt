package com.devdcrq.usastateparks.model

data class Exception(
    val endDate: String,
    var exceptionHours: ExceptionHours?,
    val name: String,
    val startDate: String
)