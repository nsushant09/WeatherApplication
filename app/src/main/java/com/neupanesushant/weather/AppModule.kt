package com.neupanesushant.weather

import android.content.Context
import android.content.SharedPreferences
import com.neupanesushant.weather.activity.main.fragment.home.HomeViewModel
import com.neupanesushant.weather.activity.main.fragment.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
fun appModule() = module {

    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    single<WeatherAPI> {
        get<Retrofit>().create(WeatherAPI::class.java)
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("WEATHER_SHARED_PREFERENCES", Context.MODE_PRIVATE)
    }

    viewModel {
        HomeViewModel(application = androidApplication(), get())
    }
    viewModel {
        SearchViewModel(application = androidApplication(), get())
    }
}