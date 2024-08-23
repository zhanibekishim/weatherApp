package com.jax.weatherapp.presentation.favourite

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

class DefaultFavouriteComponent @AssistedInject constructor(
    private val storeFactory: FavouriteStoreFactory,
    @Assisted("addToFavouriteClick") private val addToFavouriteClick: () -> Unit,
    @Assisted("onSearchClicked") private val onSearchClicked: () -> Unit,
    @Assisted("cityItemClicked") private val cityItemClicked: (City) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : FavouriteComponent, ComponentContext by componentContext {

    private val store: FavouriteStore = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect{label->
                when(label){
                    is FavouriteStore.Label.AddFavouriteClicked -> {
                        addToFavouriteClick()
                    }
                    is FavouriteStore.Label.CityItemClicked -> {
                        cityItemClicked(label.city)
                    }
                    is FavouriteStore.Label.SearchClicked -> {
                        onSearchClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavouriteStore.State> = store.stateFlow

    override fun onSearchClick() {
        store.accept(FavouriteStore.Intent.SearchClicked)
    }

    override fun onCityClick(city: City) {
        store.accept(FavouriteStore.Intent.CityItemClicked(city = city))
    }

    override fun onAddToFavouriteClick() {
        store.accept(FavouriteStore.Intent.AddFavouriteClicked)
    }

    @AssistedFactory
    interface Factory{

        fun create(
            @Assisted("addToFavouriteClick") addToFavouriteClick: () -> Unit,
            @Assisted("onSearchClicked") onSearchClicked: () -> Unit,
            @Assisted("cityItemClicked") cityItemClicked: (City) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFavouriteComponent
    }
}