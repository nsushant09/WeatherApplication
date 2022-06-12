package com.neupanesushant.weather.activity.main.fragment.home

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.weather.R
import com.neupanesushant.weather.activity.main.fragment.home.adapter.DailyForecastAdapter
import com.neupanesushant.weather.activity.main.fragment.home.adapter.HourlyForecastAdapter
import com.neupanesushant.weather.capitalizeWords
import com.neupanesushant.weather.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding

    private lateinit var viewModel : HomeViewModel
    private lateinit var application : Application

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(this, HomeViewModelFactory(this.application)).get(HomeViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvHourlyForecast.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDailyForecast.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false)

        viewModel.currentLocationWeather.observe(viewLifecycleOwner, Observer {
            val currentWeatherObject = it.current.weather.get(0)

            val hourlyAdapter = HourlyForecastAdapter(viewModel, it.hourly)
            val dailyAdapter = DailyForecastAdapter(viewModel, it.daily)
            binding.rvHourlyForecast.adapter = hourlyAdapter
            binding.rvDailyForecast.adapter = dailyAdapter
            binding.apply{
                ivHeaderWeatherIcon.setImageResource(viewModel.getWeatherIcon(currentWeatherObject.icon))
                tvHeaderWeatherDescription.text = currentWeatherObject.description.capitalizeWords()

                tvTemperatureMain.text = viewModel.convertKelvinToCelsius(it.current.temp)
                tvLocationMain.text = viewModel.getCityName(it.lat, it.lon)

                val hourlyForecastString : String = "Hourly Forecast"
                tvHourlyForecastTitle.text = hourlyForecastString

                val dailyForecastString : String = "Daily Forecast"
                tvDailyForecastTitle.text = dailyForecastString

            }

        })

    }

}