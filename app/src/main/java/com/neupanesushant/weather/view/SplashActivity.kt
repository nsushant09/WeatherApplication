package com.neupanesushant.weather.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
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
        requestPermission()
    }

    private fun startNextActivity(location: Location) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("currentLocationLatitude", location.latitude)
        intent.putExtra("currentLocationLongitude", location.longitude)
        startActivity(intent)
        finish()
    }

    private fun onLocationFetched(location: Location?) {
        if (location == null) startLocationSetting()
        startNextActivity(location!!)
    }

    private fun requestPermission() {

        if (!PermissionManager.hasFineLocationPermission(this) && !PermissionManager.hasCoarseLocationPermission(
                this
            )
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                startLocationSetting()
                return;
            }
        }
        PermissionManager.requestFineAndCoarseLocationPermission(this)
    }

    private fun getLocation() {
        CoroutineScope(Dispatchers.Main).launch {
            locationProvider.fetchLocation { location ->
                onLocationFetched(location)
            }
        }
    }

    private fun startLocationSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, SplashActivity.LOCATION_SETTING_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionManager.FINE_AND_COARSE_LOCATION_PCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }else{
                Toast.makeText(this, "Location Permission Required", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 200)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTING_CODE) {
            requestPermission()
        }
    }

}