package com.example.umainmunchies.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.umainmunchies.R
import com.example.umainmunchies.viewmodels.FilterViewModel
import com.example.umainmunchies.viewmodels.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailSheet(restaurantViewModel: RestaurantViewModel, filterViewModel: FilterViewModel) {
    val restaurant = restaurantViewModel.selectedRestaurant.collectAsState()
    val openStatus = restaurantViewModel.openStatus.collectAsState()
    if(restaurant.value == null) return
    val openStatusString = if (openStatus.value?.is_currently_open == true) "Open" else "Closed"
    val openStatusColor = if (openStatus.value?.is_currently_open == true) R.color.positive else R.color.negative

    ModalBottomSheet(
        onDismissRequest = { restaurantViewModel.setSelectedRestaurant(null) },
        dragHandle = {},
        modifier = Modifier.fillMaxSize(),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RectangleShape
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
            ) {
            AsyncImage(
                model = restaurant.value?.image_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = { restaurantViewModel.setSelectedRestaurant(null) },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 12.dp, vertical = 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Close"
                )
            }

            Card(
                elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.BottomCenter)
                    .offset(y = 100.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.Start)
                ) {
                    Text(
                        text = restaurant.value!!.name,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = filterViewModel.mapFilterIdToString(restaurant.value!!.filterIds),
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = openStatusString,
                        color = colorResource(openStatusColor),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}
