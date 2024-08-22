package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ConditionDto(
    @Expose
    @SerializedName("text") val text: String,
    @Expose
    @SerializedName("icon") val iconUrl: String
)
