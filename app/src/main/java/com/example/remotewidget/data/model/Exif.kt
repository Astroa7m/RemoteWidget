package com.example.remotewidget.data.model

data class Exif(
    val aperture: String,
    val exposure_time: String,
    val focal_length: String,
    val iso: Int,
    val make: String,
    val model: String,
    val name: String
)