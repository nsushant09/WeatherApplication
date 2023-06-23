package com.neupanesushant.weather.activity.main.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.weather.R
import com.neupanesushant.weather.Utils
import com.neupanesushant.weather.activity.main.fragment.home.adapter.DailyForecastAdapter
import com.neupanesushant.weather.activity.main.fragment.home.adapter.HourlyForecastAdapter
import com.neupanesushant.weather.activity.main.fragment.search.SearchFragment
import com.neupanesushant.weather.activity.main.fragment.settings.SettingsActivity
import com.neupanesushant.weather.apiserviceclass.LocationWeather
import com.neupanesushant.weather.capitalizeWords
import com.neupanesushant.weather.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject


class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    private val viewModel: HomeViewModel by inject()
    private val searchFragment = SearchFragment.getInstance(object : SearchFragment.SearchListener {
        override fun onSearchResultClick(cityName: String, latitude: Double, longitude: Double) {
            viewModel.getResults(latitude.toString(), longitude.toString())
        }
    })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        if (arguments == null)
            requireActivity().onBackPressed()

        val longitude = arguments!!.getString("currentLocationLongitude")
        val latitude = arguments!!.getString("currentLocationLatitude")
        viewModel.getResults(latitude.toString(), longitude.toString())


        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            tvSearchBar.setOnClickListener {
                replaceFragment(searchFragment)
            }

            ivSettingsBtn.setOnClickListener {
                Intent(requireContext(), SettingsActivity::class.java).apply {
                    startActivity(this)
                }
            }

        }
        binding.rvHourlyForecast.layoutManager =
            GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDailyForecast.layoutManager =
            GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false)


        viewModel.isLocationWeatherLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.root.visibility = View.INVISIBLE
            } else {
                binding.root.visibility = View.VISIBLE
            }
        }

        viewModel.currentLocationWeather.observe(viewLifecycleOwner) {
            setData(it)
        }

    }

    private fun setData(it: LocationWeather) {
        val currentWeatherObject = it.current.weather[0]

        val hourlyAdapter = HourlyForecastAdapter(requireContext(), it, it.hourly)
        val dailyAdapter = DailyForecastAdapter(requireContext(), it, it.daily)
        binding.rvHourlyForecast.adapter = hourlyAdapter
        binding.rvDailyForecast.adapter = dailyAdapter
        binding.apply {
            ivHeaderWeatherIcon.setImageResource(Utils.getWeatherIcon(currentWeatherObject.icon))
            tvHeaderWeatherDescription.text = currentWeatherObject.description.capitalizeWords()

            tvTemperatureMain.text = Utils.convertKelvinToCelsius(it.current.temp)
            tvLocationMain.text = setLocationName()

            Picasso.get().load(Utils.getWeatherImage(currentWeatherObject.icon)).centerCrop()
                .fit().error(Utils.getWeatherIcon(currentWeatherObject.icon))
                .into(this.ivCurrentImage)
            tvPressure.text = it.current.pressure.toInt().toString()
            tvHumidity.text = it.current.humidity.toInt().toString()

            val hourlyForecastString = "Hourly Forecast"
            tvHourlyForecastTitle.text = hourlyForecastString

            val dailyForecastString = "Daily Forecast"
            tvDailyForecastTitle.text = dailyForecastString

        }
    }

    private fun setLocationName(): String {
        return if (!viewModel.getCityName(
                viewModel.currentLocationWeather.value?.lat!!,
                viewModel.currentLocationWeather.value?.lon!!
            ).equals("nullValue", true)
        ) {
            viewModel.getCityName(
                viewModel.currentLocationWeather.value?.lat!!,
                viewModel.currentLocationWeather.value?.lon!!
            )
        } else {
            "Unknown"
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}