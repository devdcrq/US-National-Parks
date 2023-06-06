package com.devdcrq.usastateparks.model

data class Activity(
    val id: String,
    val name: String
) {
    override fun toString() = name
}