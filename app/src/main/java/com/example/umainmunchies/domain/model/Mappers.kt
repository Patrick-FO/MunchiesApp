package com.example.umainmunchies.domain.model

import com.example.umainmunchies.api.Filter
import com.example.umainmunchies.api.OpenStatus
import com.example.umainmunchies.api.Restaurant

fun Restaurant.toDomain(): RestaurantEntity {
    return RestaurantEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.image_url,
        rating = this.rating,
        filterIds = this.filterIds,
        deliveryTimeMinutes = this.delivery_time_minutes
    )
}

fun Filter.toDomain(): FilterEntity {
    return FilterEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.image_url
    )
}

fun OpenStatus.toDomain(): OpenStatusEntity {
    return OpenStatusEntity(
        isCurrentlyOpen = this.is_currently_open,
        restaurantId = this.restaurant_id
    )
}