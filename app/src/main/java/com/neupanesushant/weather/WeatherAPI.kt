package com.neupanesushant.weather

import com.neupanesushant.weather.apiserviceclass.LocationWeather
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {
    @GET("onecall")
    suspend fun getLocationWeather( @Query("lat") latitude : String , @Query("lon") longitude : String, @Query("appid") id : String ) : LocationWeather
}

