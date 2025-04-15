package com.example.umainmunchies.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
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
fun RestaurantListView(restaurantViewModel: RestaurantViewModel, filterViewModel: FilterViewModel) {
    val restaurants by restaurantViewModel.restaurants.collectAsState()
    val selectedRestaurant by restaurantViewModel.selectedRestaurant.collectAsState()

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

        LazyColumn(modifier = Modifier.fillMaxWidth(0.9f)) {
            items(restaurants) { restaurant ->
                RestaurantCard(restaurant, filterViewModel, restaurantViewModel)
            }
        }

        if (selectedRestaurant != null) {
            RestaurantDetailSheet(restaurantViewModel, filterViewModel)
        }
    }
}

