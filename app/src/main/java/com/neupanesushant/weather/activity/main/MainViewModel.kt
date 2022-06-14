package com.neupanesushant.weather.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neupanesushant.weather.LocationCoordinates

class MainViewModel(val latAndLonObj : LocationCoordinates): ViewModel() {
    private val _locationCoordinates = MutableLiveData<LocationCoordinates>()
    val locationCoordinates : LiveData<LocationCoordinates> get() = _locationCoordinates

    init{
        _locationCoordinates.value = latAndLonObj
    }

    fun setLocationCoordinates(latitude : Double, longitude : Double){
        _locationCoordinates.value = LocationCoordinates(latitude, longitude)
    }
}