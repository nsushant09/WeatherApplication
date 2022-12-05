package com.neupanesushant.weather.activity.main

import android.app.UiModeManager.MODE_NIGHT_NO
import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
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
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val viewModel : MainViewModel by viewModels()
    private val homeFragment = HomeFragment();
    private val sharedPreferences : SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isDarkMode.observe(this){
            changeTheme()
        }
        val latitude = intent.extras?.get("currentLocationLatitude") as Double
        val longitude = intent.extras?.get("currentLocationLongitude") as Double
        viewModel.setLocationCoordinates(LocationCoordinates(latitude,longitude))
        loadHomeFragment(latitude,longitude)
    }

    fun changeTheme(){
        val currentNightMode = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(currentNightMode){
            Configuration.UI_MODE_NIGHT_NO ->{
                if(sharedPreferences.getBoolean("DARK_MODE_ON", false)){
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                }
            }
            Configuration.UI_MODE_NIGHT_YES ->{
                if(!sharedPreferences.getBoolean("DARK_MODE_ON", false)){
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                }
            }
        }
    }
    fun loadHomeFragment(latitude : Double, longitude : Double){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, homeFragment).commit()
    }


}