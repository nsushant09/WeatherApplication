package com.neupanesushant.weather.data

import com.neupanesushant.weather.model.LocationWeather
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {
    @GET("onecall")
    suspend fun getLocationWeather( @Query("lat") latitude : String , @Query("lon") longitude : String, @Query("appid") id : String ) : LocationWeather
}

