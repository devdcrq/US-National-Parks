package com.devdcrq.usastateparks.model

data class Topic(
    val id: String,
    val name: String
) {
    override fun toString() = name
}