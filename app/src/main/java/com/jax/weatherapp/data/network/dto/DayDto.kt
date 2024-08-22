package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DayDto(
    @Expose
    @SerializedName("date_epoch") val date: Long,
    @Expose
    @SerializedName("day") val dayWeatherDto: DayWeatherDto
)