package com.jax.weatherapp.domain.repository

import com.jax.weatherapp.domain.entity.ForeCast

interface SearchRepository {

    suspend fun search(query: String): ForeCast
}