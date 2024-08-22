package com.jax.weatherapp.data.repository

import com.jax.weatherapp.data.mapper.toEntities
import com.jax.weatherapp.data.network.api.ApiService
import com.jax.weatherapp.domain.entity.City
import com.jax.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> {
       return apiService.searchCity(query).toEntities()
    }
}