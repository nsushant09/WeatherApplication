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

    fun getWeatherImage(description : String) : String{

        when(description){
            "01d" -> return "https://cdn.pixabay.com/photo/2016/09/14/20/03/airplane-1670266__340.jpg"
            "01n" -> return "https://cdn.pixabay.com/photo/2018/06/03/21/50/milky-way-3451655__340.jpg"
            "02d" -> return "https://cdn.pixabay.com/photo/2022/04/07/15/13/farming-7117714__340.jpg"
            "02n" -> return "https://cdn.pixabay.com/photo/2014/09/17/13/42/mackerel-sky-449475__480.jpg"
            "03d" -> return "https://cdn.pixabay.com/photo/2013/04/09/17/08/sunset-102234__480.jpg"
            "03n" -> return "https://cdn.pixabay.com/photo/2018/05/04/12/43/monolithic-part-of-the-waters-3373906__480.jpg"
            "04d" -> return "https://cdn.pixabay.com/photo/2014/11/29/18/04/window-550648__480.jpg"
            "04n" -> return "https://cdn.pixabay.com/photo/2013/07/03/17/49/moon-142977__340.jpg"
            "09d" -> return "https://cdn.pixabay.com/photo/2014/04/05/11/39/rain-316579__340.jpg"
            "09n" -> return "https://cdn.pixabay.com/photo/2015/03/11/13/54/rain-668694__480.jpg"
            "10d" -> return "https://cdn.pixabay.com/photo/2014/04/05/11/39/rain-316579__340.jpg"
            "10n" -> return "https://cdn.pixabay.com/photo/2015/03/11/13/54/rain-668694__480.jpg"
            "11d" -> return "https://cdn.pixabay.com/photo/2019/12/22/22/00/lightning-4713379__480.jpg"
            "11n" -> return "https://cdn.pixabay.com/photo/2015/09/23/08/16/thunder-953118__340.jpg"
            "13d" -> return "https://cdn.pixabay.com/photo/2012/12/13/06/39/snow-69661__480.jpg"
            "13n" -> return "https://cdn.pixabay.com/photo/2016/10/21/19/49/sunrise-1759047__340.jpg"
            else -> return "https://cdn.pixabay.com/photo/2015/11/06/09/53/trees-1025783__340.jpg"
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
