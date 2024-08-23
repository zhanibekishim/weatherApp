package com.jax.weatherapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.jax.weatherapp.presentation.details.DetailsComponent
import com.jax.weatherapp.presentation.favourite.FavouriteComponent
import com.jax.weatherapp.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>
    sealed interface Child{
        data class FavouriteChild(val component: FavouriteComponent): Child
        data class SearchChild(val component: SearchComponent): Child
        data class DetailsChild(val component: DetailsComponent): Child
    }
}