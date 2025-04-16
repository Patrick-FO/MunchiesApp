package com.example.umainmunchies.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.umainmunchies.R
import com.example.umainmunchies.viewmodels.FilterViewModel
import com.example.umainmunchies.viewmodels.RestaurantViewModel

@Composable
fun RestaurantListView(
    restaurantViewModel: RestaurantViewModel,
    filterViewModel: FilterViewModel
) {
    val restaurants by restaurantViewModel.restaurants.collectAsState()
    val selectedRestaurant by restaurantViewModel.selectedRestaurant.collectAsState()
    val isLoading by restaurantViewModel.isLoading.collectAsState()
    val error by restaurantViewModel.error.collectAsState()
    val toggledFilterIds by filterViewModel.toggledFilterIds.collectAsState()
    val filtersLoaded by filterViewModel.filtersLoaded.collectAsState()

    // Primary filter loading - do this first thing
    LaunchedEffect(Unit) {
        filterViewModel.loadAllFilters()
    }

    // Apply filters when toggled filters change
    LaunchedEffect(toggledFilterIds) {
        if (toggledFilterIds.isNotEmpty()) {
            restaurantViewModel.applyFilters(toggledFilterIds.toList())
        } else {
            restaurantViewModel.loadAllRestaurants()
        }
    }

    // Secondary loading when restaurants are available and filters aren't loaded
    LaunchedEffect(restaurants) {
        if (restaurants.isNotEmpty() && !filtersLoaded) {
            filterViewModel.loadFiltersForRestaurants(restaurants)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(R.drawable.umain_logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Start)
                .size(102.dp)
                .padding(24.dp)
        )

        FiltersRow(filterViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading && restaurants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null && restaurants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $error")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f)
            ) {
                items(
                    items = restaurants,
                    key = { it.id } // Use stable key for better recomposition
                ) { restaurant ->
                    RestaurantCard(restaurant, filterViewModel, restaurantViewModel)
                }
            }
        }

        if (selectedRestaurant != null) {
            RestaurantDetailSheet(restaurantViewModel, filterViewModel)
        }
    }
}