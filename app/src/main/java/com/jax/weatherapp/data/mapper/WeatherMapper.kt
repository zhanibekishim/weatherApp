package com.jax.weatherapp.data.mapper

import android.icu.util.Calendar
import com.jax.weatherapp.data.network.dto.WeatherCurrentDto
import com.jax.weatherapp.data.network.dto.WeatherDto
import com.jax.weatherapp.data.network.dto.WeatherForecastDto
import com.jax.weatherapp.domain.entity.Forecast
import com.jax.weatherapp.domain.entity.Weather
import java.util.Date


fun WeatherCurrentDto.toEntity(): Weather = this.current.toEntity()

fun WeatherDto.toEntity() = Weather(
    tempC = tempC,
    conditionText = condition.text,
    conditionUrl = condition.iconUrl.correctImageUrl(),
    date = date.toCalendar()
)

fun WeatherForecastDto.toEntity() = Forecast(
    currentWeather = this.currentDto.toEntity(),
    upcoming = this.forecast.forecastDay.drop(1).map{dayDto ->
        Weather(
            tempC = dayDto.dayWeatherDto.avgTempC,
            conditionText = dayDto.dayWeatherDto.condition.text,
            conditionUrl = dayDto.dayWeatherDto.condition.iconUrl.correctImageUrl(),
            date = dayDto.date.toCalendar()
        )
    }
)

private fun String.correctImageUrl() = "https:$this".replace(
    oldValue = "64x64",
    newValue = "128x128"
)
private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}


