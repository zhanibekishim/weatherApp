package com.jax.weatherapp.data.mapper

import com.jax.weatherapp.data.local.model.CityDbModel
import com.jax.weatherapp.domain.entity.City

fun CityDbModel.toEntity(): City = City(id,country,name)

fun List<CityDbModel>.toEntities(): List<City> = map { it.toEntity() }

fun City.toDbModel(): CityDbModel = CityDbModel(id,country,name)