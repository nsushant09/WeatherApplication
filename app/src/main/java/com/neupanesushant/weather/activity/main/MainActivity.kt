package com.neupanesushant.weather.activity.main

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.neupanesushant.weather.LocationCoordinates
import com.neupanesushant.weather.R
import com.neupanesushant.weather.activity.main.fragment.home.HomeFragment
import com.neupanesushant.weather.activity.main.fragment.search.SearchFragment
import com.neupanesushant.weather.activity.main.fragment.settings.SettingsFragment
import com.neupanesushant.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var viewModel : MainViewModel
    private val homeFragment = HomeFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude = intent.extras?.get("currentLocationLatitude") as Double
        val longitude = intent.extras?.get("currentLocationLongitude") as Double
        viewModel = ViewModelProvider(this, MainViewModelFactory(LocationCoordinates(latitude, longitude))).get(MainViewModel::class.java)
        loadHomeFragment(latitude, longitude)
    }

    fun loadHomeFragment(latitude : Double, longitude : Double){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putDouble("latitude", latitude)
        bundle.putDouble("longitude", longitude)
        homeFragment.arguments = bundle
        fragmentTransaction.add(R.id.fragment_container, homeFragment).commit()
    }

}