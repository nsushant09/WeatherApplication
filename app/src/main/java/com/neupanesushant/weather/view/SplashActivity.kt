package com.neupanesushant.weather.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.neupanesushant.weather.databinding.ActivitySplashBinding
import com.neupanesushant.weather.extras.PermissionManager
import com.neupanesushant.weather.services.LocationProvider
import com.neupanesushant.weather.view.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var currentLocation: Location
    private lateinit var locationProvider: LocationProvider

    companion object {
        const val LOCATION_SETTING_CODE = 1;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationProvider = LocationProvider(this)
        getCurrentLocation()
    }

    private fun startNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("currentLocationLatitude", currentLocation.latitude)
        intent.putExtra("currentLocationLongitude", currentLocation.longitude)
        startActivity(intent)
        finish()
    }


    private fun getCurrentLocation() {
        if (PermissionManager.hasCoarseLocationPermission(this) && PermissionManager.hasFineLocationPermission(
                this
            )
        ) {
            locationProvider.fetchLocation() {
                if (it == null)
                    locationProvider.startLocationSetting()
                else {
                    currentLocation = it
                    startNextActivity()
                }
            }
        } else {
            PermissionManager.requestFineAndCoarseLocationPermission(this)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionManager.FINE_AND_COARSE_LOCATION_PCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTING_CODE) {
            getCurrentLocation()
        }
    }

}