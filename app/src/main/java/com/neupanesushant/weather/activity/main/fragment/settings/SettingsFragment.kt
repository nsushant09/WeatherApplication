package com.neupanesushant.weather.activity.main.fragment.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neupanesushant.weather.activity.main.MainViewModel
import com.neupanesushant.weather.databinding.FragmentSettingsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SettingsFragment : Fragment() {

    private lateinit var _binding: FragmentSettingsBinding
    private val binding get() = _binding

    private val sharedPreferences : SharedPreferences by inject()
    private val mainViewModel : MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchDarkMode.isChecked = sharedPreferences.getBoolean("DARK_MODE_ON", false)

        binding.switchDarkMode.setOnCheckedChangeListener{ _, isChecked ->

            sharedPreferences.edit()
                .putBoolean("DARK_MODE_ON", isChecked)
                .apply()

            mainViewModel.setDarkMode(sharedPreferences.getBoolean("DARK_MODE_ON", false))

        }
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
    
}