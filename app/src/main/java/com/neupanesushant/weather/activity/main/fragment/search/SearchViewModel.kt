package com.neupanesushant.weather.activity.main.fragment.search

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.weather.WeatherAPIService
import com.neupanesushant.weather.capitalizeWords
import kotlinx.coroutines.launch
import java.util.*

class SearchViewModel(val application: Application) : ViewModel() {


    private val TAG = "SearchViewModel"
    private val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
    private val KEY: String = "23c28e4ade04201b9448d391e0cf9832"

    private val _addressList = MutableLiveData<List<Address>>()
    val addressList: LiveData<List<Address>>
        get() = _addressList

    private val arrayListOfLocationWeather = ArrayList<LocationDetail>()

    private val _searchedLocationDetailsList = MutableLiveData<List<LocationDetail>>()
    val searchedLocationDetailsList: LiveData<List<LocationDetail>> get() = _searchedLocationDetailsList

    private val geocoder = Geocoder(application)

    private val _isNoResultFound = MutableLiveData<Boolean>()
    val isNoResultFound: LiveData<Boolean> get() = _isNoResultFound

    init {
        _isNoResultFound.value = false
    }

    fun getSearchResult(cityName: String) {
        arrayListOfLocationWeather.clear()
        try {
            _addressList.value = geocoder.getFromLocationName(cityName, 10)
            if (addressList.value != null && addressList.value?.size != 0) {
                _addressList.value?.forEach {
                    getLocationWeatherFromAPI(
                        it.latitude.toString(),
                        it.longitude.toString(),
                        it,
                        cityName
                    )
                }

            } else {
                _isNoResultFound.value = true
            }

        } catch (e: Exception) {
            _isNoResultFound.value = true
        }
    }


    fun getCityName(lat: Double, long: Double): String {
        val cityName: String
        val geoCoder = Geocoder(application, Locale.getDefault())
        val Address = geoCoder.getFromLocation(lat, long, 1)
        try {
            cityName = Address.get(0).locality
            return cityName
        } catch (e: NullPointerException) {
        }
        return "nullValue"
    }

    fun getLocationWeatherFromAPI(
        latitude: String,
        longitude: String,
        address: Address,
        cityName: String
    ) {
        viewModelScope.launch {
            try {
                val temp =
                    WeatherAPIService.retrofitService.getLocationWeather(latitude, longitude, KEY)
                arrayListOfLocationWeather.add(
                    LocationDetail(
                        if (getCityName(
                                temp.lat,
                                temp.lon
                            ) == "nullValue"
                        ) cityName.capitalizeWords() else getCityName(
                            temp.lat,
                            temp.lon
                        ),
                        if (address.countryCode == null) "" else address.countryCode,
                        temp.lat,
                        temp.lon,
                        (temp.current.temp - 273.15).toInt()
                    )
                )
                _searchedLocationDetailsList.value = arrayListOfLocationWeather
                _isNoResultFound.value = false
            } catch (e: Exception) {
                _isNoResultFound.value = true
            }
        }

    }

    fun convertKelvinToCelsius(tempInKelvin: Double): String {
        return String.format("%02.1f??", tempInKelvin - 273.15)
    }


    data class LocationDetail(
        val name: String,
        val countryName: String,
        val latitude: Double,
        val longitude: Double,
        val currentTemperature: Int
    )

    override fun onCleared() {
        super.onCleared()
    }
}