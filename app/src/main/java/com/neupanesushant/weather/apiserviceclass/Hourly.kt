package com.neupanesushant.weather.apiserviceclass

data class Hourly(
    val clouds: Double,
    val dt: Long,
    val pressure: Double,
    val rain: Rain,
    val temp: Double,
    val uvi: Double,
    val visibility: Double,
    val weather: List<WeatherXX>,
    val wind_deg: Double,
    val wind_gust: Double,
    val wind_speed: Double
)