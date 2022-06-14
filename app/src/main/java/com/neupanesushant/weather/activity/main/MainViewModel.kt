package com.neupanesushant.weather.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neupanesushant.weather.LocationCoordinates

class MainViewModel(val latAndLonObj : LocationCoordinates): ViewModel() {
    private val _locationCoordinates = MutableLiveData<LocationCoordinates>()
    val locationCoordinates : LiveData<LocationCoordinates> get() = _locationCoordinates

    private val _locationName = MutableLiveData<String>()
    val locationName :LiveData<String> get() = _locationName
    init{
        _locationCoordinates.value = latAndLonObj
    }

    fun setLocationCoordinates(cityName : String, latitude : Double, longitude : Double){
        _locationCoordinates.value = LocationCoordinates(latitude, longitude)
        _locationName.value = cityName
    }
}