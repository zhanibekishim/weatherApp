package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForeCastDayDto(
    @Expose
    @SerializedName("forecastday") val forecastDay: List<DayDto>
)
