package com.jax.weatherapp.domain.entity

import java.util.Calendar

data class Weather(
    val date: Calendar,
    val temperature: Float,
    val description: String,
    val iconUrl: String
)
