package com.jax.weatherapp.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.jax.weatherapp.domain.entity.City
import com.jax.weatherapp.domain.usecase.GetFavouritesUseCase
import com.jax.weatherapp.domain.usecase.GetWeatherUseCase
import com.jax.weatherapp.presentation.favourite.FavouriteStore.Intent
import com.jax.weatherapp.presentation.favourite.FavouriteStore.Label
import com.jax.weatherapp.presentation.favourite.FavouriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object SearchClicked : Intent
        data object AddFavouriteClicked : Intent
        data class CityItemClicked(val city: City) : Intent
    }

    data class State(
        val cityItems: List<CityItem>
    ) {
        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {

            data object Initial : WeatherState
            data object WeatherLoading : WeatherState
            data object WeatherError : WeatherState

            data class WeatherLoaded(
                val conditionUrl: String,
                val tempC: Float
            ) : WeatherState
        }
    }

    sealed interface Label {

        data object SearchClicked : Label
        data object AddFavouriteClicked : Label
        data class CityItemClicked(val city: City) : Label
    }
}

class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) {

    fun create(): FavouriteStore =
        object : FavouriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavouriteStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class CitiesLoaded(val cities: List<City>): Action
    }

    private sealed interface Msg {

        data class CitiesLoaded(val cities: List<City>): Msg

        data class WeatherLoading(val cityId: Int): Msg
        data class WeatherError(val cityId: Int): Msg
        data class WeatherLoaded(
            val cityId: Int?,
            val tempC: Float?,
            val conditionIconUrl: String?
        ) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouritesUseCase().collect{citiesFromDb ->
                    dispatch(Action.CitiesLoaded(citiesFromDb))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.AddFavouriteClicked -> {
                    publish(Label.AddFavouriteClicked)
                }
                is Intent.CityItemClicked -> {
                    publish(Label.CityItemClicked(intent.city))
                }
                is Intent.SearchClicked -> {
                    publish(Label.SearchClicked)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when(action){
                is Action.CitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.CitiesLoaded(cities))
                    cities.forEach{city->
                        scope.launch {
                            loadWeather(city.id)
                        }
                    }
                }
            }
        }
        suspend fun loadWeather(cityId: Int){
            dispatch(Msg.WeatherLoading(cityId))
            try {
                val weather = getWeatherUseCase(cityId)
                dispatch(Msg.WeatherLoaded(
                    cityId = cityId,
                    tempC = weather.tempC,
                    conditionIconUrl = weather.conditionUrl)
                )
            }catch (e:Exception){
                dispatch(Msg.WeatherError(cityId))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg){
            is Msg.CitiesLoaded -> {
                copy(
                    cityItems = msg.cities.map{city->
                        State.CityItem(
                            city = city,
                            weatherState = State.WeatherState.WeatherLoading
                        )
                    }
                )
            }
            is Msg.WeatherLoading -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = State.WeatherState.WeatherLoading)
                        } else {
                            it
                        }
                    }
                )
            }
            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.WeatherLoaded(
                                    tempC = msg.tempC ?: 0f,
                                    conditionUrl = msg.conditionIconUrl ?: ""
                                )
                            )
                        } else {
                            it
                        }
                    }
                )
            }


            is Msg.WeatherError -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = State.WeatherState.WeatherError)
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }
}
