package com.example.umainmunchies.domain.model

data class RestaurantEntity(
    val id: String,
    val name: String,
    val imageUrl: String,
    val rating: Float,
    val filterIds: List<String>,
    val deliveryTimeMinutes: Int
)

data class FilterEntity(
    val id: String,
    val name: String,
    val imageUrl: String?
)

data class OpenStatusEntity(
    val isCurrentlyOpen: Boolean,
    val restaurantId: String?,
)