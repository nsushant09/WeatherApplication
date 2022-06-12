package com.neupanesushant.weather.apiserviceclass

data class Daily(
    val clouds: Double,
    val dt: Long,
    val sunrise : Long,
    val sunset: Long,
    val pressure: Double,
    val rain: Double,
    val temp: Temp,
    val uvi: Double,
    val weather: List<WeatherX>,
    val wind_deg: Double,
    val wind_gust: Double,
    val wind_speed: Double
)