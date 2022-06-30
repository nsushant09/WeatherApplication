package com.neupanesushant.weather

import com.neupanesushant.weather.apiserviceclass.LocationWeather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


private val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface WeatherAPI {
    @GET("onecall")
    suspend fun getLocationWeather( @Query("lat") latitude : String , @Query("lon") longitude : String, @Query("appid") id : String ) : LocationWeather
}

object WeatherAPIService{
    val retrofitService : WeatherAPI by lazy{ retrofit.create(WeatherAPI::class.java)}
}