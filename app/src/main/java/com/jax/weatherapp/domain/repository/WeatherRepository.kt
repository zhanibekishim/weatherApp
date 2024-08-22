package com.jax.weatherapp.domain.repository

import com.jax.weatherapp.domain.entity.Forecast
import com.jax.weatherapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather
    suspend fun getForecast(cityId: Int): Forecast
}