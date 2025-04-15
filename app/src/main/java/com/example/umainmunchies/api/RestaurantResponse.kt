package com.example.umainmunchies.api

data class RestaurantResponse(
    val restaurants: List<Restaurant>
)

data class Restaurant(
    val id: String,
    val name: String,
    val rating: Float,
    val filterIds: List<String>,
    val image_url: String,
    val delivery_time_minutes: Int
)

data class Filter(
    val id: String,
    val name: String,
    val image_url: String
)

data class Error(
    val error: Boolean,
    val reason: String
)

data class OpenStatus(
    val restaurant_id: String,
    val is_currently_open: Boolean
)