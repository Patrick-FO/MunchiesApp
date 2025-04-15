package com.example.umainmunchies.api

import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantApiService {
    @GET("restaurants")
    suspend fun getAllRestaurants(): RestaurantResponse

    @GET("filter/{id}")
    suspend fun getFilter(@Path("id") filterId: String): Filter

    @GET("open/{id}")
    suspend fun getOpenStatus(@Path("id") restaurantId: String): OpenStatus
}
