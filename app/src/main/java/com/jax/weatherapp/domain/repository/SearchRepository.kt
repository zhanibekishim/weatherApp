package com.jax.weatherapp.domain.repository

import com.jax.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}