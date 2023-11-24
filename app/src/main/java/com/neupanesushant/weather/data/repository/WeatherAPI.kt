package com.neupanesushant.weather.data.repository

import com.neupanesushant.weather.domain.model.LocationWeather
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {
    @GET("onecall")
    suspend fun getLocationWeather( @Query("lat") latitude : String , @Query("lon") longitude : String, @Query("appid") id : String ) : LocationWeather
}

