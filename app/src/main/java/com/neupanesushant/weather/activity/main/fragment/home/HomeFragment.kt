package com.neupanesushant.weather.activity.main.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.weather.R
import com.neupanesushant.weather.activity.main.MainViewModel
import com.neupanesushant.weather.activity.main.fragment.home.adapter.DailyForecastAdapter
import com.neupanesushant.weather.activity.main.fragment.home.adapter.HourlyForecastAdapter
import com.neupanesushant.weather.activity.main.fragment.search.SearchFragment
import com.neupanesushant.weather.activity.main.fragment.settings.SettingsFragment
import com.neupanesushant.weather.apiserviceclass.LocationWeather
import com.neupanesushant.weather.capitalizeWords
import com.neupanesushant.weather.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject


class HomeFragment : Fragment() {

    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding

    private val viewModel : HomeViewModel by inject()

    private val searchFragment = SearchFragment()
    private val settingFragment = SettingsFragment()

    private val  mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        mainViewModel.locationCoordinates.observe(viewLifecycleOwner) {
            viewModel.getResults(it.latitude.toString(), it.longitude.toString())
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun setData(it : LocationWeather){
        val currentWeatherObject = it.current.weather[0]

        val hourlyAdapter = HourlyForecastAdapter(requireContext(), viewModel, it.hourly)
        val dailyAdapter = DailyForecastAdapter(requireContext(),viewModel, it.daily)
        binding.rvHourlyForecast.adapter = hourlyAdapter
        binding.rvDailyForecast.adapter = dailyAdapter
        binding.apply{
            ivHeaderWeatherIcon.setImageResource(viewModel.getWeatherIcon(currentWeatherObject.icon))
            tvHeaderWeatherDescription.text = currentWeatherObject.description.capitalizeWords()

            tvTemperatureMain.text = viewModel.convertKelvinToCelsius(it.current.temp)
            tvTemperatureMain.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
            tvLocationMain.text = setLocationName()
            tvLocationMain.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)

            Picasso.get().load(viewModel.getWeatherImage(currentWeatherObject.icon)).centerCrop().fit().error(viewModel.getWeatherIcon(currentWeatherObject.icon)).into(this.ivCurrentImage)
            ivCurrentImage.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
            tvPressure.text = it.current.pressure.toInt().toString()
            tvHumidity.text = it.current.humidity.toInt().toString()

            val hourlyForecastString = "Hourly Forecast"
            tvHourlyForecastTitle.text = hourlyForecastString

            val dailyForecastString = "Daily Forecast"
            tvDailyForecastTitle.text = dailyForecastString

        }
    }

    private fun setLocationName() : String{
        return if(!viewModel.getCityName(viewModel.currentLocationWeather.value?.lat!!, viewModel.currentLocationWeather.value?.lon!!).equals("nullValue", true)){
            viewModel.getCityName(viewModel.currentLocationWeather.value?.lat!!, viewModel.currentLocationWeather.value?.lon!!)
        }else if(mainViewModel.locationName.value != null){
            mainViewModel.locationName.value!!
        }else{
            "Unknown"
        }
    }
    private fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.isAddToBackStackAllowed
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}