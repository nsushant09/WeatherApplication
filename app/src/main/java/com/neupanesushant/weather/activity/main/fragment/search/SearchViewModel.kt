package com.neupanesushant.weather.activity.main.fragment.search

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neupanesushant.weather.WeatherAPI
import com.neupanesushant.weather.apiserviceclass.LocationWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    val isNoResultFound : LiveData<Boolean> get() = _isNoResultFound

    init{
        _isNoResultFound.value = false
    }

    fun getSearchResult(cityName: String) {
        arrayListOfLocationWeather.clear()
        try {
            _addressList.value = geocoder.getFromLocationName(cityName, 10)
            if (addressList.value != null && addressList.value?.size != 0) {
                _addressList.value?.forEach {
                    getLocationWeatherFromAPI(it.latitude.toString(), it.longitude.toString(), it)
                }
            }else{
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
        cityName = Address.get(0).locality
        return cityName
    }

    fun getLocationWeatherFromAPI(latitude: String, longitude: String, address: Address) {
        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherAPI::class.java)

        val retrofitData = retrofit.getLocationWeather(latitude, longitude, KEY)

        retrofitData.enqueue(object : Callback<LocationWeather> {
            override fun onResponse(
                call: Call<LocationWeather>,
                response: Response<LocationWeather>
            ) {
                if (response != null) {
                    arrayListOfLocationWeather.add(
                        LocationDetail(
                            getCityName(
                                response.body()!!.lat,
                                response.body()!!.lon
                            ),
                            address.countryCode,
                            response.body()!!.lat,
                            response.body()!!.lon,
                            (response.body()!!.current.temp - 273.15).toInt()
                        )
                    )
                    _searchedLocationDetailsList.value = arrayListOfLocationWeather
                    _isNoResultFound.value = false
                }
            }

            override fun onFailure(call: Call<LocationWeather>, t: Throwable) {
                _isNoResultFound.value = true
            }

        })
    }

    fun convertKelvinToCelsius(tempInKelvin: Double): String {
        return String.format("%02.1f°", tempInKelvin - 273.15)
    }


    data class LocationDetail(
        val name: String,
        val countryName: String,
        val latitude: Double,
        val longitude: Double,
        val currentTemperature: Int
    )
}