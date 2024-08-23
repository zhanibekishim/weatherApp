package com.jax.weatherapp.di

import android.content.Context
import com.jax.weatherapp.data.local.db.FavouriteCitiesDao
import com.jax.weatherapp.data.local.db.FavouriteDatabase
import com.jax.weatherapp.data.network.api.ApiFactory
import com.jax.weatherapp.data.network.api.ApiService
import com.jax.weatherapp.data.repository.FavouriteRepositoryImpl
import com.jax.weatherapp.data.repository.SearchRepositoryImpl
import com.jax.weatherapp.data.repository.WeatherRepositoryImpl
import com.jax.weatherapp.domain.repository.FavouriteRepository
import com.jax.weatherapp.domain.repository.SearchRepository
import com.jax.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService = ApiFactory.apiService

        @[ApplicationScope Provides]
        fun provideFavouriteDatabase(context: Context): FavouriteDatabase {
            return FavouriteDatabase.getInstance(context)
        }

        @[ApplicationScope Provides]
        fun provideFavouriteCitiesDao(database: FavouriteDatabase): FavouriteCitiesDao {
            return database.favouriteCitiesDao()
        }
    }
}