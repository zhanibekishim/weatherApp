package com.jax.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.jax.weatherapp.presentation.details.DetailsContent
import com.jax.weatherapp.presentation.favourite.FavouriteContent
import com.jax.weatherapp.presentation.search.SearchContent
import com.jax.weatherapp.ui.WeatherAppTheme

@Composable
fun RootContent(component: RootComponent){

    WeatherAppTheme {
        Children(
            stack = component.stack
        ) {
            when(val instance = it.instance){
                is RootComponent.Child.DetailsChild -> {
                    DetailsContent(component = instance.component)
                }
                is RootComponent.Child.FavouriteChild -> {
                    FavouriteContent(component = instance.component)
                }
                is RootComponent.Child.SearchChild -> {
                    SearchContent(component = instance.component)
                }
            }
        }
    }
}