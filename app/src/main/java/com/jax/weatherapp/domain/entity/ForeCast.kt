package com.jax.weatherapp.domain.entity

data class ForeCast(
    val city: City,
    val list: List<Weather>
)
