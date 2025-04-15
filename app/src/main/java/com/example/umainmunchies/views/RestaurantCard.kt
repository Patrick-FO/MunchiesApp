package com.example.umainmunchies.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.umainmunchies.R
import com.example.umainmunchies.api.Restaurant
import com.example.umainmunchies.viewmodels.FilterViewModel
import com.example.umainmunchies.viewmodels.RestaurantViewModel

@Composable
fun RestaurantCard(
    restaurantObject: Restaurant,
    filterViewModel: FilterViewModel,
    restaurantViewModel: RestaurantViewModel
) {
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 4.dp,
        pressedElevation = 2.dp
    ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier.clickable {
            restaurantViewModel.setSelectedRestaurant(restaurantObject)
        }
    ) {
        Column() {
            AsyncImage(
                model = restaurantObject.image_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = restaurantObject.name, fontSize = 20.sp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.star_icon),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = restaurantObject.rating.toString(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = colorResource(R.color.rating_color)
                        )
                    }
                }
                Row() {
                    Text(
                        text = filterViewModel.mapFilterIdToString(restaurantObject.filterIds),
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = restaurantViewModel.formatTime(restaurantObject.delivery_time_minutes.toFloat()),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}