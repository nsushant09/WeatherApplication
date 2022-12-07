package com.neupanesushant.weather.activity.main.fragment.settings

import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.neupanesushant.weather.R
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
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchDarkMode.isChecked = sharedPreferences.getBoolean("DARK_MODE_ON", false)

        binding.switchDarkMode.setOnCheckedChangeListener{btn , isChecked ->

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