package com.jax.weatherapp.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.jax.weatherapp.domain.entity.City
import com.jax.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultSearchComponent @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted("onCityClicked") private val onCityClicked: (City) -> Unit,
    @Assisted("onSavedToFavourite") private val onSavedToFavourite: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext{

    private val store: SearchStore = instanceKeeper.getStore { storeFactory.create(openReason) }
    private val scope = componentScope()

    init{
        scope.launch {
            store.labels.collect{label->
                when(label){
                    is SearchStore.Label.ClickBack -> {
                        onClickBack()
                    }
                    is SearchStore.Label.OpenForecast -> {
                        onCityClicked(label.city)
                    }
                    is SearchStore.Label.SavedToFavourite -> {
                        onSavedToFavourite()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun onBackClicked() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onSearchClick() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onChangeQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeQuery(query = query))
    }

    @AssistedFactory
    interface Factory{

        fun create(
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("onCityClicked") onCityClicked: (City) -> Unit,
            @Assisted("onSavedToFavourite") onSavedToFavourite: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ):DefaultSearchComponent
    }

}