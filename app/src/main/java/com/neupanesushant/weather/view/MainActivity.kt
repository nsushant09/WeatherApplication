package com.neupanesushant.weather.view


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neupanesushant.weather.R
import com.neupanesushant.weather.view.home.HomeFragment
import com.neupanesushant.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val latitude = intent.extras?.get("currentLocationLatitude") as Double
        val longitude = intent.extras?.get("currentLocationLongitude") as Double
        loadHomeFragment(latitude.toString(), longitude.toString())
    }

    private fun loadHomeFragment(latitude: String, longitude: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("currentLocationLatitude", latitude)
        bundle.putString("currentLocationLongitude", longitude)
        homeFragment.arguments = bundle
        fragmentTransaction.add(R.id.fragment_container, homeFragment).commit()
    }
}