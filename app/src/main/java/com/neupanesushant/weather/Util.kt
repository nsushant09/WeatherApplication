package com.neupanesushant.weather

import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import java.util.*

fun String.capitalizeWords(): String = split(" ").map { it.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.ROOT
    ) else it.toString()
} }.joinToString(" ")


data class LocationCoordinates(
    val latitude : Double,
    val longitude : Double
)