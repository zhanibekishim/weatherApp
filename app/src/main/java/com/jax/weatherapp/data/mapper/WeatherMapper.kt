package com.jax.weatherapp.data.mapper

import android.icu.util.Calendar
import com.jax.weatherapp.data.network.dto.WeatherCurrentDto
import com.jax.weatherapp.data.network.dto.WeatherDto
import com.jax.weatherapp.data.network.dto.WeatherForecastDto
import com.jax.weatherapp.domain.entity.Forecast
import com.jax.weatherapp.domain.entity.Weather
import java.util.Date


fun WeatherCurrentDto.toEntity(): Weather = current.toEntity()

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    conditionText = condition.text,
    conditionUrl = condition.iconUrl.correctImageUrl(),
    date = date.toCalendar()
)

fun WeatherForecastDto.toEntity() = Forecast(
    currentWeather = currentDto.toEntity(),
    upcoming = forecast.forecastDay.drop(1).map { dayDto ->
        val dayWeatherDto = dayDto.dayWeatherDto
        Weather(
            tempC = dayWeatherDto.avgTempC,
            conditionText = dayWeatherDto.condition.text,
            conditionUrl = dayWeatherDto.condition.iconUrl.correctImageUrl(),
            date = dayDto.date.toCalendar()
        )
    }
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun String.correctImageUrl() = "https:$this".replace(
    oldValue = "64x64",
    newValue = "128x128"
)

