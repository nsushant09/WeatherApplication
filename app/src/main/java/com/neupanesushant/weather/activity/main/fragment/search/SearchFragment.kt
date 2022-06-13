package com.neupanesushant.weather.activity.main.fragment.search

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.KeyListener
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.EditorInfo.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.neupanesushant.weather.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import java.security.Key


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
        setupSearchBar()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupSearchBar()
        binding.ivHomeBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.etSearchBar.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if(actionId == IME_ACTION_SEARCH || actionId == IME_ACTION_DONE){
                    binding.etSearchBar.text.apply{
                        if(this != null && this.length != 0){
                            viewModel.getSearchResult(this.toString())
                        }
                    }
                }
                return false
            }

        })

    }



    fun setupSearchBar(){
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearchBar, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.etSearchBar.setTextCursorDrawable(0)
        }
        binding.etSearchBar.requestFocus()
    }



}