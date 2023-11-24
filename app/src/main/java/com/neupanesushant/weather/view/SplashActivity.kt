package com.neupanesushant.weather.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.neupanesushant.weather.databinding.ActivitySplashBinding
import com.neupanesushant.weather.domain.extras.PermissionManager
import com.neupanesushant.weather.domain.usecase.LocationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
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

    private fun startNextActivity(location: Location) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("currentLocationLatitude", location.latitude)
        intent.putExtra("currentLocationLongitude", location.longitude)
        startActivity(intent)
        finish()
    }

    private fun onLocationFetched(location: Location?) {
        if (location == null) locationProvider.startLocationSetting()
        startNextActivity(location!!)
    }

    private fun getCurrentLocation() {

        if (!checkPermission()) {
            PermissionManager.requestFineAndCoarseLocationPermission(this)
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            locationProvider.fetchLocation { location ->
                onLocationFetched(location)
            }
        }
    }

    private fun checkPermission(): Boolean {
        return PermissionManager.hasCoarseLocationPermission(this)
                && PermissionManager.hasFineLocationPermission(this)
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