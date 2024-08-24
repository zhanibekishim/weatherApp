package com.jax.weatherapp.domain.entity

import android.icu.util.Calendar
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val tempC: Float,
    val conditionText: String,
    val conditionUrl: String,
    val date:Calendar
):Parcelable