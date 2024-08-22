package com.jax.weatherapp.domain.usecase

import com.jax.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForeCaseUseCast @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend fun getForeCast(cityId: Int) = repository.getForeCast(cityId)
}