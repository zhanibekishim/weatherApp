package com.jax.weatherapp.domain.usecase

import com.jax.weatherapp.domain.entity.City
import com.jax.weatherapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class ChangeFavouriteStateUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    suspend fun addToFavourites(city: City){
        repository.addToFavourite(city)
    }
    suspend fun removeFromFavourites(cityId: Int){
        repository.removeFromFavourite(cityId)
    }
}