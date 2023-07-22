package com.neupanesushant.weather.view.main.fragment.home

import android.app.Application
import android.location.Geocoder
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.weather.data.WeatherAPI
import com.neupanesushant.weather.model.LocationWeather
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(val application : Application, private val retrofitInstance : WeatherAPI) : ViewModel() {

    private val KEY : String = "23c28e4ade04201b9448d391e0cf9832"

    private val _currentLocationWeather = MutableLiveData<LocationWeather>()
    val currentLocationWeather : LiveData<LocationWeather> get() = _currentLocationWeather

    private val _isLocationWeatherLoading = MutableLiveData<Boolean>()
    val isLocationWeatherLoading : LiveData<Boolean> get() = _isLocationWeatherLoading


    fun getResults(latitude : String, longitude : String ){
        getLocationWeatherFromAPI(latitude, longitude)
    }
    fun getCityName(lat: Double, long: Double): String {
        val cityName: String
        val geoCoder = Geocoder(application, Locale.getDefault())
        try{
            val address = geoCoder.getFromLocation(lat, long, 1)
            cityName = address[0].locality
            return cityName
        }catch (e : NullPointerException){
        }
        return "nullValue"
    }


    private fun getLocationWeatherFromAPI(latitude : String, longitude : String ){
        _isLocationWeatherLoading.value = true
        viewModelScope.launch{
            try{
                _currentLocationWeather.value = retrofitInstance.getLocationWeather(latitude, longitude, KEY)
                _isLocationWeatherLoading.value = false
            }catch(e : Exception){
                Toast.makeText(application, "Error Occured", Toast.LENGTH_SHORT).show()
                _isLocationWeatherLoading.value = false
            }
        }
    }
}
