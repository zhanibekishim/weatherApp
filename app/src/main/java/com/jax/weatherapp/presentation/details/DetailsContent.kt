package com.jax.weatherapp.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jax.weatherapp.R
import com.jax.weatherapp.domain.entity.Forecast
import com.jax.weatherapp.domain.entity.Weather
import com.jax.weatherapp.presentation.extensions.formattedDay
import com.jax.weatherapp.presentation.extensions.formattedFullDate
import com.jax.weatherapp.presentation.extensions.toCelsius
import com.jax.weatherapp.ui.CardGradients

@Composable
fun DetailsContent(component: DetailsComponent){

    val state by component.model.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CardGradients.gradients[1].primaryGradient),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                cityName = state.city.name,
                isFavourite = state.isFavourite,
                clickBack = { component.onClickBack() },
                changeFavoriteStatus = { component.onClickChangeFavouriteStatus() }
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)){
            when(val forecastState = state.forecastState){
                is DetailsStore.State.ForecastState.Error -> {
                    Error()
                }

                is DetailsStore.State.ForecastState.Loaded -> {
                    Forecast(forecastState.forecast)
                }

                is DetailsStore.State.ForecastState.Loading -> {
                    Loading()
                }
                is DetailsStore.State.ForecastState.Initial -> {}
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    cityName: String,
    isFavourite: Boolean,
    clickBack: () -> Unit,
    changeFavoriteStatus: () -> Unit
){
    CenterAlignedTopAppBar(
        title = { Text(text = cityName) },
        actions = {
            IconButton(onClick = { changeFavoriteStatus() }) {
                val iconFavourite = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                Icon(imageVector = iconFavourite, contentDescription = null)
            }
        },
        navigationIcon = {
            IconButton(onClick = { clickBack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Forecast(
    forecast: Forecast
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = forecast.currentWeather.conditionText,
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = forecast.currentWeather.tempC.toCelsius(),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 70.sp)
            )
            GlideImage(
                modifier = Modifier.size(100.dp),
                model = forecast.currentWeather.conditionUrl,
                contentDescription = null
            )
        }
        Text(
            text = forecast.currentWeather.date.formattedFullDate(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        AnimatedUpcomingWeather(forecast.upcoming)
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun UpcomingWeather(
    upcoming: List<Weather>
){
    Card(
        modifier = Modifier
            .padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.28f)),
        shape = MaterialTheme.shapes.extraLarge
    ){
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(R.string.upcoming),
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                upcoming.forEach{weather->
                    SmallWeatherCard(weather)
                }
            }
        }
    }
}

@Composable
fun AnimatedUpcomingWeather(
    upcoming: List<Weather>
){
    val state = remember{
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = { IntOffset(0, it.height) }
        )
    ){
        UpcomingWeather(upcoming)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RowScope.SmallWeatherCard(
    weather: Weather
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .height(128.dp)
            .weight(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = weather.tempC.toCelsius())
            GlideImage(
                modifier = Modifier.size(48.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
            Text(text = weather.date.formattedDay())
        }
    }
}

@Composable
fun Loading(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ){
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun Error(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ){
        Text(stringResource(R.string.happened_some_issue))
    }
}




