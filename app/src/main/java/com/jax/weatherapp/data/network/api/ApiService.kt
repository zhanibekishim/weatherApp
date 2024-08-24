package com.jax.weatherapp.data.network.api

import com.jax.weatherapp.data.network.dto.CityDto
import com.jax.weatherapp.data.network.dto.WeatherCurrentDto
import com.jax.weatherapp.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json?key=95e822ced91b4e7595b101502242208")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ): WeatherCurrentDto

    @GET("forecast.json?key=95e822ced91b4e7595b101502242208")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") daysCount: Int = 4,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): WeatherForecastDto

    @GET("search.json?key=95e822ced91b4e7595b101502242208")
    suspend fun searchCity(
        @Query("q") query: String
    ): List<CityDto>
}