package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @Expose
    @SerializedName("forecast")val forecast: ForeCastDayDto,
    @Expose
    @SerializedName("current")val currentDto: WeatherCurrentDto
)
