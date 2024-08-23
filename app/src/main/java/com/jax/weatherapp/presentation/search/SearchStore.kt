package com.jax.weatherapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.jax.weatherapp.domain.entity.City
import com.jax.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.jax.weatherapp.domain.usecase.SearchUseCase
import com.jax.weatherapp.presentation.search.SearchStore.Intent
import com.jax.weatherapp.presentation.search.SearchStore.Label
import com.jax.weatherapp.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickSearch: Intent
        data object ClickBack: Intent
        data class ClickCity(val city: City): Intent
        data class ChangeQuery(val query: String): Intent
    }

    data class State(
        val query: String,
        val state: SearchState
    ){

        sealed interface SearchState {

            data object Initial : SearchState
            data object Loading : SearchState
            data object Error : SearchState
            data object EmptyResult : SearchState
            data class SuccessLoaded(val cities: List<City>) : SearchState
        }
    }

    sealed interface Label {

        data object ClickBack : Label
        data object SavedToFavourite : Label
        data class OpenForecast(val city: City) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase,
    private val searchUseCase: SearchUseCase
){

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                query = "",
                state = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = {
                ExecutorImpl(openReason)
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangeQuery(val query: String): Msg

        data object SearchLoading: Msg
        data object SearchLoadingError: Msg
        data class SearchResultLoaded(val cities: List<City>) : Msg
    }


    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val openReason: OpenReason
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var onlyOneSearchQueryJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.ChangeQuery -> {
                    dispatch(Msg.ChangeQuery(intent.query))
                }
                is Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
                is Intent.ClickCity -> {
                    when(openReason){
                        OpenReason.AddToFavourite -> {
                            scope.launch {
                                changeFavouriteStateUseCase.addToFavourites(intent.city)
                                publish(Label.SavedToFavourite)
                            }
                        }
                        OpenReason.RegularSearch -> {
                            publish(Label.OpenForecast(intent.city))
                        }
                    }
                }
                is Intent.ClickSearch -> {
                    onlyOneSearchQueryJob?.cancel()
                    onlyOneSearchQueryJob = scope.launch {
                        dispatch(Msg.SearchLoading)
                        try {
                            val cities = searchUseCase(getState().query)
                            dispatch(Msg.SearchResultLoaded(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.SearchLoadingError)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg){
            is Msg.ChangeQuery -> {
                copy(
                    query = msg.query
                )
            }
            is Msg.SearchLoading -> {
                copy(
                    state = State.SearchState.Loading
                )
            }
            is Msg.SearchLoadingError -> {
                copy(
                    state = State.SearchState.Error
                )
            }
            is Msg.SearchResultLoaded -> {
                if(msg.cities.isEmpty()){
                    copy(state = State.SearchState.SuccessLoaded(msg.cities))
                }else{
                    copy(state = State.SearchState.EmptyResult)
                }
            }
        }
    }
}
