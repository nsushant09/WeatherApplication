package com.neupanesushant.weather.activity.main.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.weather.activity.main.fragment.home.HomeViewModel
import com.neupanesushant.weather.apiserviceclass.Daily
import com.neupanesushant.weather.capitalizeWords
import com.neupanesushant.weather.databinding.DailyForecastRecyclerviewLayoutBinding

class DailyForecastAdapter(val context : Context, val viewModel : HomeViewModel, val list : List<Daily>) : RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {
    inner class ViewHolder(binding : DailyForecastRecyclerviewLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val icon = binding.ivIcon
        val weatherType = binding.tvWeatherType
        val temperature = binding.tvTemperature
        val sunriseTime = binding.tvSunRiseTime
        val sunsetTime = binding.tvSunSetTime
        val day = binding.tvDayName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DailyForecastRecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyObject = list.get(position)
        holder.itemView.animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_slide_in_top)
        holder.icon.setImageResource(viewModel.getWeatherIcon(dailyObject.weather.get(0).icon))
        holder.weatherType.text = dailyObject.weather.get(0).main.capitalizeWords()
        holder.temperature.text = viewModel.convertKelvinToCelsius(dailyObject.temp.day)
        holder.sunriseTime.text = viewModel.currentLocationWeather.value?.timezone_offset?.toLong()
            ?.let { viewModel.convertTimeToLocalTime(dailyObject.sunrise, it) }
        holder.sunsetTime.text = viewModel.currentLocationWeather.value?.timezone_offset?.toLong()
            ?.let { viewModel.convertTimeToLocalTime(dailyObject.sunset, it) }

        holder.day.text = viewModel.convertTimeToDay(dailyObject.dt)

    }
    override fun getItemCount(): Int {
        if(list.size > 7){
            return 7
        }else{
            return list.size
        }
    }
}