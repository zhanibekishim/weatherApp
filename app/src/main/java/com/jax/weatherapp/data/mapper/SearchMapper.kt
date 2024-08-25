package com.jax.weatherapp.data.mapper

import com.jax.weatherapp.data.network.dto.CityDto
import com.jax.weatherapp.domain.entity.City

fun CityDto.toEntity() = City(id,name,country)

fun List<CityDto>.toEntities() = map { it.toEntity() }