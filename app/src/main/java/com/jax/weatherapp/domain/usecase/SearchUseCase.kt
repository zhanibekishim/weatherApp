package com.jax.weatherapp.domain.usecase

import com.jax.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    suspend fun search(query: String) = searchRepository.search(query)
}