package com.neupanesushant.weather.activity.main


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.neupanesushant.weather.LocationCoordinates
import com.neupanesushant.weather.R
import com.neupanesushant.weather.activity.main.fragment.home.HomeFragment
import com.neupanesushant.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    private val homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val latitude = intent.extras?.get("currentLocationLatitude") as Double
        val longitude = intent.extras?.get("currentLocationLongitude") as Double
        viewModel.setLocationCoordinates(LocationCoordinates(latitude, longitude))
        loadHomeFragment()
    }

    private fun loadHomeFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, homeFragment).commit()
    }
}