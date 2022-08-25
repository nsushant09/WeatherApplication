package com.neupanesushant.weather.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.neupanesushant.weather.databinding.ActivitySplashBinding
import com.neupanesushant.weather.activity.main.MainActivity
//import com.neupanesushant.weather.currentLocation
//import com.neupanesushant.weather.isCurrentLocationSet
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var fusedLocationProviderClient :FusedLocationProviderClient
    private lateinit var binding : ActivitySplashBinding
    private lateinit var handler : Handler
    private lateinit var runnable : Runnable
    private  var openedNextActivity = false

    var isCurrentLocationSet = false
    lateinit var currentLocation : Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        handler = Handler()

        runnable = object : Runnable{
            override fun run() {
                if(isCurrentLocationSet && !openedNextActivity){
                    startNextActivity()
                    openedNextActivity = true
                }
                handler.postDelayed(this, 500)
            }

        }
    }

    override fun onRestart() {
        super.onRestart()
        getCurrentLocation()
    }
    fun startNextActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("currentLocationLatitude", currentLocation.latitude)
        intent.putExtra("currentLocationLongitude", currentLocation.longitude)
        startActivity(intent)
        finish()
    }




    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){

        if(checkPermissions()){
            if(isLocationEnabled()){
                //get the final location
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
                    val location : Location?= task.result
                    if (location != null) {
                        currentLocation = location
                        isCurrentLocationSet = true
                        handler.postDelayed(runnable, 1000)
                    }else{
                        getNewLocation()
                    }
                }
            }else{
                //settings opens here
                Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            //request the permission here
            requestPermissions()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION  = 100
    }

    private fun checkPermissions() : Boolean {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }else{
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationEnabled() : Boolean{
        val locationManager : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    @SuppressLint("MissingPermission")
    private fun getNewLocation(){
        val locationrequest = LocationRequest()
        locationrequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationrequest.interval = 0
        locationrequest .fastestInterval = 0
        locationrequest.numUpdates = 2
        Looper.myLooper()?.let {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationrequest,locationCallback, it
            )
        }
    }
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            currentLocation = p0.lastLocation
            isCurrentLocationSet = true
            handler.postDelayed(runnable, 1000)
        }
    }

}