package com.jax.weatherapp.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityDto(
    @Expose
    @SerializedName("id")val id: Long,
    @Expose
    @SerializedName("name")val name: String,
    @Expose
    @SerializedName("country")val country: String,
)
