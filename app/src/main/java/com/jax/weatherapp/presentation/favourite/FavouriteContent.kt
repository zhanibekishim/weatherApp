package com.jax.weatherapp.presentation.favourite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jax.weatherapp.R
import com.jax.weatherapp.presentation.extensions.toCelsius
import com.jax.weatherapp.ui.CardGradients
import com.jax.weatherapp.ui.Gradient
import com.jax.weatherapp.ui.Purple40

@Composable
fun FavouriteContent(
    component: FavouriteComponent
) {
    val state by component.model.collectAsState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item(span = {GridItemSpan(2)}) {
            SearchCard {
                component.onSearchClick()
            }
        }
        itemsIndexed(
            items = state.cityItems,
            key = { _, item -> item.city.id }
        ) { index, item ->
            CityCard(
                cityItem = item,
                index = index,
                onClick = {
                    component.onCityClick(item.city)
                }
            )
        }
        item {
            AddFavouriteCard{
                component.onAddToFavouriteClick()
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CityCard(
    onClick: () -> Unit,
    cityItem: FavouriteStore.State.CityItem,
    index: Int
) {
    val gradient = getGradient(index)
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                spotColor = gradient.shadowColor,
                shape = MaterialTheme.shapes.extraLarge
            ),
        shape = MaterialTheme.shapes.extraLarge
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .sizeIn(minHeight = 196.dp)
                .background(
                    brush = gradient.primaryGradient,
                    shape = RoundedCornerShape(10.dp),
                )
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x - size.width / 10,
                            y = center.y + size.height / 2
                        ),
                        radius = size.maxDimension / 2
                    )
                },
        ) {
            when(val state = cityItem.weatherState){
                is FavouriteStore.State.WeatherState.WeatherError -> {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(48.dp),
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = null
                    )
                }
                is FavouriteStore.State.WeatherState.WeatherLoaded -> {
                    GlideImage(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(60.dp)
                            .padding(4.dp),
                        model = state.conditionUrl,
                        contentDescription = null
                    )
                    Text(modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 25.dp),
                        text = state.tempC.toCelsius(),
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp)
                    )
                }
                is FavouriteStore.State.WeatherState.WeatherLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.background
                    )
                }
                is FavouriteStore.State.WeatherState.Initial -> {}
            }

            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                text = cityItem.city.name,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun AddFavouriteCard(
    onClick: () -> Unit
){
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.extraLarge,
        border =  BorderStroke(width = 1.dp,MaterialTheme.colorScheme.onBackground)
    ){
        Column(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxSize()
                .sizeIn(minHeight = 196.dp)
                .padding(24.dp)
        ){
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .size(48.dp),
                imageVector = Icons.Default.Edit,
                tint = Purple40,
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.add_city_to_favourite),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 10.sp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}


@Composable
fun SearchCard(
    onClick: ()->Unit
){
    val gradient = CardGradients.gradients[4]

    Card(
        shape = CircleShape
    ){
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .background(brush = gradient.primaryGradient),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    ),
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.search),
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
fun getGradient(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}

