package com.neupanesushant.weather.activity.main.fragment.home

import android.app.Application
import android.location.Geocoder
import android.util.Log
import android.util.TimeUtils
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neupanesushant.weather.R
import com.neupanesushant.weather.WeatherAPI
import com.neupanesushant.weather.apiserviceclass.LocationWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HomeViewModel(val application : Application) : ViewModel() {

    private val TAG : String = "HomeViewModel"
    private val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
    private val BASE_URL_CITY_COORD : String = "http://api.openweathermap.org/geo/1.0/"
    private val KEY : String = "23c28e4ade04201b9448d391e0cf9832"

    private val _currentLocationWeather = MutableLiveData<LocationWeather>()
    val currentLocationWeather : LiveData<LocationWeather> get() = _currentLocationWeather

//    init{
//        getLocationWeatherFromAPI(currentLocation.latitude.toString(), currentLocation.longitude.toString())
//    }

    fun getResults(latitude : String, longitude : String ){
        getLocationWeatherFromAPI(latitude, longitude)
    }
    fun getCityName(lat: Double, long: Double): String {
        val cityName: String
        val geoCoder = Geocoder(application, Locale.getDefault())
        val Address = geoCoder.getFromLocation(lat, long, 1)
        try{
            cityName = Address.get(0).locality
            return cityName
        }catch (e : NullPointerException){
            Log.i(TAG,"Null pointer exception")
        }
        return "nullValue"
    }

    fun getCountryName( lat : Double , long : Double ) : String{
        val countryName : String
        val geoCoder = Geocoder(application, Locale.getDefault())
        val Address = geoCoder.getFromLocation(lat, long,1)
        countryName = Address.get(0).countryName
        return countryName
    }


    fun getLocationWeatherFromAPI(latitude : String, longitude : String ){
        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherAPI::class.java)

        val retrofitData = retrofit.getLocationWeather(latitude, longitude, KEY)

        retrofitData.enqueue(object : Callback<LocationWeather>{
            override fun onResponse(
                call: Call<LocationWeather>,
                response: Response<LocationWeather>
            ) {
                if(response != null){
                    _currentLocationWeather.value  = response.body()!!
                }
            }

            override fun onFailure(call: Call<LocationWeather>, t: Throwable) {
                Toast.makeText(application, "Error Occured", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getWeatherIcon(description : String) : Int {
        if(description.equals("01d",true) || description.equals("01n",true)){
            return R.drawable.ic_clearsky
        }else if(description.equals("02d", true) || description.equals("02n",true) ){
            return R.drawable.ic_fewclouds
        }else if (description.equals("03d", true) || description.equals("03n",true)){
            return R.drawable.ic_scatteredcloud
        }else if(description.equals("04d", true) || description.equals("04n",true)){
            return R.drawable.ic_broken_clouds
        }else if(description.equals("09d", true) || description.equals("09n",true)){
            return R.drawable.ic_showerrain
        }else if(description.equals("10d", true) || description.equals("10n",true)){
            return R.drawable.ic_rain
        }else if(description.equals("11d", true) || description.equals("11n",true)){
            return R.drawable.ic_thunderstorm
        }else if(description.equals("13d", true) || description.equals("13n",true)){
            return R.drawable.ic_snow
        }else{
            return R.drawable.ic_mist
        }
    }

    fun convertKelvinToCelsius(tempInKelvin : Double) : String{
        return String.format("%02.1f°", tempInKelvin - 273.15)
    }

    fun convertTimeToLocalTime(time: Long, offset: Long) : String{
        val finalTime = time + offset
        val hours = TimeUnit.SECONDS.toHours(finalTime) % 24
        val minutes = TimeUnit.SECONDS.toMinutes(finalTime) % 60

        val timeString = String.format("%02d:%02d", hours, minutes)
        return timeString

    }

    fun convertTimeToDay(time : Long) : String{
        var day : Int = TimeUnit.SECONDS.toDays(time).toInt()
        day %= 7
        when(day){
            1 -> return "Saturday"
            2 -> return "Sunday"
            3 -> return "Monday"
            4 -> return "Tuesday"
            5 -> return "Wednesday"
            6 -> return "Thursday"
            0 -> return "Friday"
            else -> return "Error"
        }
    }
}
