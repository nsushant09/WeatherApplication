package com.neupanesushant.weather.activity.main.fragment.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.neupanesushant.weather.R
import com.neupanesushant.weather.activity.main.MainViewModel
import com.neupanesushant.weather.databinding.FragmentSettingsBinding
import org.koin.android.compat.SharedViewModelCompat.sharedViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SettingsActivity : AppCompatActivity() {


    private lateinit var binding : FragmentSettingsBinding
    private val sharedPreferences : SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AnimationUtils.loadAnimation(binding.root.context, org.koin.android.R.anim.abc_fade_in)
        setupView()
        setupEventListener()
    }

    private fun setupView(){
        binding.switchDarkMode.isChecked = sharedPreferences.getBoolean("DARK_MODE_ON", false)
    }

    private fun setupEventListener(){
        binding.switchDarkMode.setOnCheckedChangeListener{ _, isChecked ->

            sharedPreferences.edit()
                .putBoolean("DARK_MODE_ON", isChecked)
                .apply()

            if(isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()

        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
    
}