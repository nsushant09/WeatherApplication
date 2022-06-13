package com.neupanesushant.weather.activity.main.fragment.search

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.neupanesushant.weather.R
import com.neupanesushant.weather.activity.main.fragment.home.HomeFragment
import com.neupanesushant.weather.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var _binding : FragmentSearchBinding
    private val binding get() = _binding

    private lateinit var viewModel : SearchViewModel
    private lateinit var application : Application

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(this, SearchViewModelFactory(this.application)).get(SearchViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupSearchBar()
        binding.ivHomeBtn.setOnClickListener {
            parentFragmentManager.apply {
                popBackStack()
                popBackStack()
            }
        }


    }

    fun setupSearchBar(){
        binding.etSearchBar.requestFocus()
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearchBar, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.etSearchBar.setTextCursorDrawable(0)
        }
    }
    fun searchBarChangeListener(){
        binding.etSearchBar.addTextChangedListener {
            if(it == null || it.length == 0){

            }else{

            }
        }
    }


}