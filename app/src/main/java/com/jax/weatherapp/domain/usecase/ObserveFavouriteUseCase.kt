package com.jax.weatherapp.domain.usecase

import com.jax.weatherapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class ObserveFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    operator fun invoke(cityId: Int) = repository.observeIsFavourite(cityId)
}