package com.neupanesushant.weather.view.main.fragment.search

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.weather.R
import com.neupanesushant.weather.view.main.fragment.home.HomeFragment
import com.neupanesushant.weather.view.main.fragment.search.adapter.SearchResultAdapter
import com.neupanesushant.weather.databinding.FragmentSearchBinding
import org.koin.android.ext.android.inject


class SearchFragment() : Fragment() {

    private lateinit var _binding: FragmentSearchBinding
    private val binding get() = _binding

    private val viewModel: SearchViewModel by inject()

    private val onSearchedResultClick: (String, Double, Double) -> Unit =
        { cityName, latitude, longitude ->
            val home = HomeFragment()
            val bundle = Bundle()
            bundle.putString("currentLocationLatitude", latitude.toString())
            bundle.putString("currentLocationLongitude", longitude.toString())
            home.arguments = bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, home)
            fragmentTransaction.commit()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        setupSearchBar()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupSearchBar()
        binding.apply {
            llSearchResults.visibility = View.GONE
            progressBar.visibility = View.GONE
            tvNoResultFound.visibility = View.GONE
        }
        binding.ivHomeBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.etSearchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_SEARCH || actionId == IME_ACTION_DONE) {
                binding.etSearchBar.text.apply {
                    if (this != null && this.isNotEmpty()) {
                        binding.apply {
                            llSearchResults.visibility = View.GONE
                            progressBar.visibility = View.VISIBLE
                            tvNoResultFound.visibility = View.GONE
                        }
                        viewModel.getSearchResult(this.toString())
                    }
                }
            }
            false
        }

        viewModel.isNoResultFound.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvNoResultFound.visibility = View.VISIBLE
                binding.llSearchResults.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            } else {
                binding.tvNoResultFound.visibility = View.GONE
            }
        }

        viewModel.searchedLocationDetailsList.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            if (it.isEmpty() || it == null) {
                binding.tvNoResultFound.visibility = View.VISIBLE
            } else {
                binding.rvSearchResults.layoutManager = LinearLayoutManager(context)
                val adapter = SearchResultAdapter(viewModel, it, onSearchedResultClick)
                binding.rvSearchResults.adapter = adapter
                binding.llSearchResults.visibility = View.VISIBLE
            }
        }

    }


    private fun setupSearchBar() {
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearchBar, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.etSearchBar.setTextCursorDrawable(0)
        }
        binding.etSearchBar.requestFocus()
    }


    companion object {

        private var instance: SearchFragment? = null
        fun getInstance(): SearchFragment {
            if (instance == null) {
                instance = SearchFragment()
            }
            return instance!!
        }
    }
}