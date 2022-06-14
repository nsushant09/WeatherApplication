package com.neupanesushant.weather.activity.main.fragment.search.adapter

import android.annotation.SuppressLint
import android.location.Address
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.weather.activity.main.fragment.search.SearchViewModel
import com.neupanesushant.weather.apiserviceclass.LocationWeather
import com.neupanesushant.weather.databinding.SearchresultRecyclerviewLayoutBinding

class SearchResultAdapter(val viewModel: SearchViewModel, val list: List<SearchViewModel.LocationDetail>, val onSearchResultClick: (String, Double, Double) -> Unit) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    inner class ViewHolder(binding : SearchresultRecyclerviewLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val cityName = binding.tvCityName
        val countryName = binding.tvCountryName
        val latitude = binding.tvLatitude
        val longitude = binding.tvLongitude
        val currentTemperature = binding.tvCurrentTemperature
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchresultRecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObject = list.get(position)
        holder.cityName.text = currentObject.name
        holder.countryName.text = ", ${currentObject.countryName}"
        holder.latitude.text = String.format("Lat : %02.2f", currentObject.latitude)
        holder.longitude.text = String.format("Lon : %02.2f", currentObject.longitude)
        holder.currentTemperature.text = currentObject.currentTemperature.toString()
        holder.itemView.setOnClickListener{
            onSearchResultClick(currentObject.name,currentObject.latitude, currentObject.longitude)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}