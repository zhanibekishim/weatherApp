package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @Expose
    @SerializedName("avgtemp_c") val avgTempC: Float,
    @Expose
    @SerializedName("condition") val condition: ConditionDto
)
