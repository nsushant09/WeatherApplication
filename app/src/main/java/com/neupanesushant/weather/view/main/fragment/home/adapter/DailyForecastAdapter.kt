package com.neupanesushant.weather.view.main.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.weather.extras.Utils
import com.neupanesushant.weather.model.Daily
import com.neupanesushant.weather.model.LocationWeather
import com.neupanesushant.weather.capitalizeWords
import com.neupanesushant.weather.databinding.DailyForecastRecyclerviewLayoutBinding

class DailyForecastAdapter(
    val context: Context,
    private val currentLocationWeather: LocationWeather,
    private val list: List<Daily>
) : RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {
    inner class ViewHolder(binding: DailyForecastRecyclerviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val icon = binding.ivIcon
        val weatherType = binding.tvWeatherType
        val temperature = binding.tvTemperature
        val sunriseTime = binding.tvSunRiseTime
        val sunsetTime = binding.tvSunSetTime
        val day = binding.tvDayName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DailyForecastRecyclerviewLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyObject = list[position]
        holder.itemView.animation =
            AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_slide_in_top)
        holder.icon.setImageResource(Utils.getWeatherIcon(dailyObject.weather[0].icon))
        holder.weatherType.text = dailyObject.weather[0].main.capitalizeWords()
        holder.temperature.text = Utils.convertKelvinToCelsius(dailyObject.temp.day)
        holder.sunriseTime.text = currentLocationWeather.timezone_offset?.toLong()
            ?.let { Utils.convertTimeToLocalTime(dailyObject.sunrise, it) }
        holder.sunsetTime.text = currentLocationWeather.timezone_offset?.toLong()
            ?.let { Utils.convertTimeToLocalTime(dailyObject.sunset, it) }

        holder.day.text = Utils.convertTimeToDay(dailyObject.dt)

    }

    override fun getItemCount(): Int {
        return if (list.size > 7) {
            7
        } else {
            list.size
        }
    }
}