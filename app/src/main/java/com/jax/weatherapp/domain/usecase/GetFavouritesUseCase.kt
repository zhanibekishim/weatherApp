package com.jax.weatherapp.domain.usecase

import com.jax.weatherapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class GetFavouritesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    operator fun invoke() = repository.favouriteCities
}