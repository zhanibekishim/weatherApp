package com.jax.weatherapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forecast(
    val currentWeather: Weather,
    val upcoming: List<Weather>
):Parcelable