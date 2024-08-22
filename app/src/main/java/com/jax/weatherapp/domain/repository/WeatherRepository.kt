package com.jax.weatherapp.domain.repository

import com.jax.weatherapp.domain.entity.ForeCast
import com.jax.weatherapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather
    suspend fun getForeCast(cityId: Int): ForeCast
}