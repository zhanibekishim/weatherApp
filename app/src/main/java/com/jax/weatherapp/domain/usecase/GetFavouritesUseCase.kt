package com.jax.weatherapp.domain.usecase

import com.jax.weatherapp.domain.repository.FavouriteRepository

class GetFavouritesUseCase(
    private val repository: FavouriteRepository
) {
    operator fun invoke() = repository.favouriteCities
}