package com.neupanesushant.weather.services

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

class LocationProvider(private val activity: Activity) {
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    private var location: Location? = null

    @SuppressLint("MissingPermission")
    fun fetchLocation(onCompletionListener: (Location?) -> Unit) {
        if (isLocationEnabled()) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { locationTask ->
                if (locationTask.result != null)
                    location = locationTask.result
                else
                    getNewLocationData()

                onCompletionListener(location)
            }
        }else{
            onCompletionListener(null)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        Looper.myLooper()?.let {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        location = locationResult.lastLocation
                    }
                },
                it
            )
        }
    }

    fun startLocationSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivityForResult(intent, SplashActivity.LOCATION_SETTING_CODE)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

}