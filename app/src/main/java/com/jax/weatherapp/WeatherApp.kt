package com.jax.weatherapp

import android.app.Application
import com.jax.weatherapp.di.ApplicationComponent
import com.jax.weatherapp.di.ApplicationScope
import com.jax.weatherapp.di.DaggerApplicationComponent

@ApplicationScope
class WeatherApp: Application() {

    private lateinit var applicationComponent: ApplicationComponent


    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}