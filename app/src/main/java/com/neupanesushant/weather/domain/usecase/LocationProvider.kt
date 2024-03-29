package com.neupanesushant.weather.domain.usecase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.neupanesushant.weather.view.SplashActivity
import kotlinx.coroutines.coroutineScope

class LocationProvider(private val activity: Activity) {
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    @SuppressLint("MissingPermission")
    suspend fun fetchLocation(onCompletionListener: (Location?) -> Unit) = coroutineScope {
        if (!isLocationEnabled()) onCompletionListener(null)

        fusedLocationProviderClient.lastLocation
            .addOnCompleteListener { locationTask ->
                if (locationTask.result != null) {
                    onCompletionListener(locationTask.result)
                } else {
                    getNewLocationData(onCompletionListener)
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocationData(onCompletionListener: (Location?) -> Unit) {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        Looper.myLooper()?.let {

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        onCompletionListener(locationResult.lastLocation)
                    }
                },
                it
            )

        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

}