package com.neupanesushant.weather

import android.app.Application
import android.location.Geocoder
import android.location.Location
import java.util.*

var isCurrentLocationSet = false
lateinit var currentLocation : Location

fun String.capitalizeWords(): String = split(" ").map { it.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.ROOT
    ) else it.toString()
} }.joinToString(" ")
