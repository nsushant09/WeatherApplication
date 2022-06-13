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
import java.lang.Exception

class SearchViewModel(val application : Application) : ViewModel() {


    private val TAG = "SearchViewModel"
    private val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
    private val KEY : String = "23c28e4ade04201b9448d391e0cf9832"

    private val _addressList = MutableLiveData<List<Address>>()
    val addressList : LiveData<List<Address>>
        get() = _addressList

    private val _locationWeatherList = MutableLiveData<List<LocationWeather>>()
    val locationWeatherList : LiveData<List<LocationWeather>> get() = _locationWeatherList

    private val arrayListOfLocationWeather = ArrayList<LocationWeather>()

    private val geocoder = Geocoder(application)

    fun getSearchResult(cityName : String){
        try{
            _addressList.value = geocoder.getFromLocationName(cityName, 10)
            if(addressList.value != null && addressList.value?.size != 0){
                _addressList.value?.forEach {
                    getLocationWeatherFromAPI(it.latitude.toString(), it.longitude.toString())
                    Log.i(TAG, "Name : ${it.countryName}")
                }
            }
        }catch (e : Exception){
            Log.i(TAG, "Exception Occured")
        }
    }

    fun getLocationWeatherFromAPI(latitude : String, longitude : String ){
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
                if(response != null){
                    arrayListOfLocationWeather.add(response.body()!!)
                    _locationWeatherList.value = arrayListOfLocationWeather
                }
            }

            override fun onFailure(call: Call<LocationWeather>, t: Throwable) {
                Toast.makeText(application, "Error Occured", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun convertKelvinToCelsius(tempInKelvin : Double) : String{
        return String.format("%02.1f°", tempInKelvin - 273.15)
    }


}