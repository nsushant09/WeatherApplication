package com.neupanesushant.weather.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.neupanesushant.weather.R
import com.neupanesushant.weather.activity.main.fragment.home.HomeFragment
import com.neupanesushant.weather.activity.main.fragment.search.SearchFragment
import com.neupanesushant.weather.activity.main.fragment.settings.SettingsFragment
import com.neupanesushant.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val homeFragment = HomeFragment();
    private val settingsFragment = SettingsFragment()
    private val searchFragment = SearchFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(homeFragment)

    }

    fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val animation = AnimationUtils.loadAnimation(baseContext, androidx.appcompat.R.anim.abc_slide_in_bottom)
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            if(fragment != homeFragment){
                fragmentTransaction.isAddToBackStackAllowed
                fragmentTransaction.addToBackStack(null)
            }
            fragmentTransaction.commit()
        }
    }

}