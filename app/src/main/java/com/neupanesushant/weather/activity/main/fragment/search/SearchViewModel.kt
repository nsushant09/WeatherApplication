package com.neupanesushant.weather.activity.main.fragment.search

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception

class SearchViewModel(val application : Application) : ViewModel() {


    private val TAG = "SearchViewModel"

    private val _addressList = MutableLiveData<List<Address>>()
    val addressList : LiveData<List<Address>>
        get() = _addressList

    private val geocoder = Geocoder(application)

    init{
        getCoordinates("statue of liberty")
    }

    fun getCoordinates(cityName : String){
        try{
            _addressList.value = geocoder.getFromLocationName(cityName, 10)
            if(addressList.value != null){
                Log.i(TAG, "There are total ${addressList.value!!.size } results")
                Log.i(TAG, "The coordinates is lat : ${addressList.value?.get(0)?.latitude} and lon : ${addressList.value?.get(0)?.longitude}")
            }
        }catch (e : Exception){
            Log.i(TAG, "Exception Occured")
        }
    }


}