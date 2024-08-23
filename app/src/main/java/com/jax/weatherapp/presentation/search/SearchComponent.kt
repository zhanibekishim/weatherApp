package com.jax.weatherapp.presentation.search

import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun onBackClicked()
    fun onSearchClick()
    fun onChangeQuery(query: String)
}