package com.neupanesushant.weather.activity.main.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.weather.activity.main.fragment.home.HomeViewModel
import com.neupanesushant.weather.apiserviceclass.Hourly
import com.neupanesushant.weather.capitalizeWords
import com.neupanesushant.weather.databinding.HourlyForecastRecyclerviewLayoutBinding

class HourlyForecastAdapter(val context : Context, val viewModel : HomeViewModel, private val list : List<Hourly>) : RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

    inner class ViewHolder(binding : HourlyForecastRecyclerviewLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val icon = binding.ivIcon
        val time = binding.tvTime
        val weatherType = binding.tvWeatherType
        val temperature = binding.tvTemperature
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HourlyForecastRecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObject = list[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_slide_in_top)
        holder.icon.setImageResource(viewModel.getWeatherIcon(currentObject.weather[0].icon))
        holder.temperature.text = viewModel.convertKelvinToCelsius(currentObject.temp)
        holder.weatherType.text = currentObject.weather[0].description.capitalizeWords()
        holder.time.text = viewModel.currentLocationWeather.value?.timezone_offset?.toLong()
            ?.let { viewModel.convertTimeToLocalTime(currentObject.dt, it) }
    }

    override fun getItemCount(): Int{
        return if(list.size > 24){
            24
        }else{
            list.size
        }
    }

}