package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherCurrentDto(
    @Expose
    @SerializedName("current") val current: WeatherDto
)