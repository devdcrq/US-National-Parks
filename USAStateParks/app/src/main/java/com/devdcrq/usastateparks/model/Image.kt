package com.devdcrq.usastateparks.model

data class Image(
    val altText: String,
    val caption: String,
    val credit: String,
    val title: String,
    val url: String
) {
    override fun toString() = url
}