package com.neupanesushant.weather.activity.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neupanesushant.weather.LocationCoordinates

class MainViewModelFactory(val latAndLonObj : LocationCoordinates, val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(latAndLonObj, sharedPreferences) as T
        }
        throw IllegalArgumentException("Message for Illegal Argument Exception in Main View Model Factory")
    }
}