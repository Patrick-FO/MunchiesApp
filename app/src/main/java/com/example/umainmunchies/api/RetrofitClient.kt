package com.example.umainmunchies.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://food-delivery.umain.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val restaurantApiService: RestaurantApiService = retrofit.create(RestaurantApiService::class.java)
}