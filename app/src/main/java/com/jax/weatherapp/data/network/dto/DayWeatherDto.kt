package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("avgtemp_c") val avgTempC: Float,
    @SerializedName("condition") val condition: ConditionDto
)
