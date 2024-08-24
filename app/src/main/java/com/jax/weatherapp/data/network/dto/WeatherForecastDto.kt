package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("forecast")val forecast: ForeCastDayDto,
    @SerializedName("current")val currentDto: WeatherDto
)
