package com.jax.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jax.weatherapp.data.network.api.ApiFactory
import com.jax.weatherapp.ui.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = ApiFactory.apiService

        CoroutineScope(Dispatchers.IO).launch {
            val currentWeather = apiService.loadCurrentWeather("London")
            val forecast = apiService.loadForecast("London")
            val cities = apiService.searchCity("London")
            Log.d(
                "DATAAAAAAS",
                "Current Weather: $currentWeather\nForecase Weather: $forecast\nCities:$cities"
            )
        }
        setContent {
            WeatherAppTheme {

            }
        }
    }
}