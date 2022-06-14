package com.neupanesushant.weather.activity.main.fragment.home

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.weather.*
import com.neupanesushant.weather.activity.main.fragment.home.adapter.DailyForecastAdapter
import com.neupanesushant.weather.activity.main.fragment.home.adapter.HourlyForecastAdapter
import com.neupanesushant.weather.activity.main.fragment.search.SearchFragment
import com.neupanesushant.weather.activity.main.fragment.settings.SettingsFragment
import com.neupanesushant.weather.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding

    private lateinit var viewModel : HomeViewModel
    private lateinit var application : Application

    private val searchFragment = SearchFragment()
    private val settingFragment = SettingsFragment()

    var locationLatitude : Double = 0.0
    var locationLongitude : Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(this, HomeViewModelFactory(this.application)).get(HomeViewModel::class.java)
        val bundle = this.arguments
        if(bundle != null){
            locationLatitude = bundle!!.getDouble("latitude")
            locationLongitude = bundle!!.getDouble("longitude")
        }
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getResults(locationLatitude.toString() , locationLongitude.toString())
        binding.apply{

            tvSearchBar.setOnClickListener {
                replaceFragment(searchFragment)
            }

            ivSettingsBtn.setOnClickListener {
                replaceFragment(settingFragment)
            }

        }
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

    @SuppressLint("PrivateResource")
    fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(androidx.fragment.R.animator.fragment_fade_enter, androidx.fragment.R.animator.fragment_fade_exit)
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.isAddToBackStackAllowed
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}