package com.jax.weatherapp.presentation.search

import com.jax.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun onBackClicked()
    fun onSearchClick()
    fun onChangeQuery(query: String)
    fun onClickCity(city: City)
}