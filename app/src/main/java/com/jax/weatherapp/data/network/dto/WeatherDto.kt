package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.concurrent.locks.Condition

data class WeatherDto(
    @Expose
    @SerializedName("last_updated_epoch") val date: Long,
    @Expose
    @SerializedName("temp_c") val tempC: Float,
    @Expose
    @SerializedName("condition") val condition: ConditionDto
)
