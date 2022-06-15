package com.neupanesushant.weather.activity.main.fragment.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.neupanesushant.weather.R
import com.neupanesushant.weather.activity.main.MainViewModel
import com.neupanesushant.weather.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var _binding: FragmentSettingsBinding
    private val binding get() = _binding

    private val mainViewModel: MainViewModel by activityViewModels()

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

    }

}