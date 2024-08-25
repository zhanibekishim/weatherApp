package com.jax.weatherapp.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jax.weatherapp.R
import com.jax.weatherapp.domain.entity.City
import com.jax.weatherapp.ui.Pink40
import com.jax.weatherapp.ui.Pink80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(component: SearchComponent) {
    val state by component.model.collectAsState()

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    var isSearchBarActive by remember {
        mutableStateOf(false)
    }

    val searchBarColor = if (isSearchBarActive) Pink40 else Pink80

    Box(modifier = Modifier.fillMaxSize()){
        SearchBar(
            modifier = Modifier.focusRequester(focusRequester),
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
                inputFieldColors = inputFieldColors(
                    focusedTextColor = searchBarColor,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            ),
            placeholder = { Text(text = stringResource(R.string.search))},
            query = state.query,
            onQueryChange = {
                component.onChangeQuery(it)
                component.onSearchClick()
            },
            onSearch = { component.onSearchClick() },
            active = true,
            onActiveChange = {isActive ->
                isSearchBarActive = isActive
            },
            leadingIcon = {
                IconButton(onClick = { component.onBackClicked() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = { component.onSearchClick() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }
        ) {
            when (val searchState = state.state) {
                SearchStore.State.SearchState.EmptyResult -> {
                    Text(
                        text = stringResource(R.string.noting_found),
                        modifier = Modifier.padding(8.dp)
                    )
                }

                SearchStore.State.SearchState.Error -> {
                    Text(
                        text = stringResource(R.string.something_went_wrong),
                        modifier = Modifier.padding(8.dp)
                    )
                }

                SearchStore.State.SearchState.Initial -> {

                }

                SearchStore.State.SearchState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                is SearchStore.State.SearchState.SuccessLoaded -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(
                            items = searchState.cities,
                            key = { it.id }
                        ) {
                            CityCard(
                                city = it,
                                onCityClick = {city->
                                    component.onClickCity(city)
                                }
                            )
                        }
                    }
                }
            }
        }
        BackHandler {
            component.onBackClicked()
        }
    }
}

@Composable
private fun CityCard(
    city: City,
    onCityClick: (City) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCityClick(city) }
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = city.country)
        }
    }
}
