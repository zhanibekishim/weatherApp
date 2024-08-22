package com.jax.weatherapp.domain.usecase

import com.jax.weatherapp.domain.repository.WeatherRepository

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend fun getWeather(cityId: Int) = repository.getWeather(cityId)
}