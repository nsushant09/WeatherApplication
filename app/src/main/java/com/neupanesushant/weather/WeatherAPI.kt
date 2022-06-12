package com.neupanesushant.weather

import com.neupanesushant.weather.apiserviceclass.LocationWeather
import com.neupanesushant.weather.citycoordinates.CityDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherAPI {

    @GET("direct")
    fun getCityCoordinates(@Query("q") cityName : String, @Query("appid") id : String) : Call<List<CityDetails>>

    @GET("onecall")
    fun getLocationWeather( @Query("lat") latitude : String , @Query("lon") longitude : String, @Query("appid") id : String ) : Call<LocationWeather>
}