package com.jax.weatherapp.presentation.favourite

import com.jax.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {

    val model: StateFlow<FavouriteStore.State>

    fun onSearchClick()
    fun onCityClick(city: City)
    fun onAddToFavouriteClick()
}