package com.neupanesushant.weather.activity.main.fragment.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(val application : Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(application) as T
        }
        throw IllegalArgumentException("Message for Illegal Argument Exception in Search View Model Factory")
    }
}