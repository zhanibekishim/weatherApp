package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForeCastDayDto(
    @SerializedName("forecastday") val forecastDay: List<DayDto>
)