package com.example.umainmunchies.repositories

import com.example.umainmunchies.api.Filter
import com.example.umainmunchies.api.OpenStatus
import com.example.umainmunchies.api.Restaurant
import com.example.umainmunchies.api.RestaurantApiService

class RestaurantRepository(
    private val apiService: RestaurantApiService
) {
    private var cachedRestaurants: List<Restaurant>? = null

    suspend fun getAllRestaurants(): Result<List<Restaurant>> {
        return try {
            val response = apiService.getAllRestaurants()
            cachedRestaurants = response.restaurants
            Result.success(response.restaurants)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFilter(filterId: String): Result<Filter> {
        return try {
            val filter = apiService.getFilter(filterId)
            Result.success(filter)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOpenStatus(restaurantId: String): Result<OpenStatus> {
        return try {
            val status = apiService.getOpenStatus(restaurantId)
            Result.success(status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRestaurantsByFilterId(filters: List<Filter>): Result<List<Restaurant>> {
        return try {
            val restaurants = cachedRestaurants ?: run {
                val response = apiService.getAllRestaurants()
                cachedRestaurants = response.restaurants
                response.restaurants
            }

            val filterIds = filters.map { it.id }

            val filteredRestaurants = restaurants.filter { restaurant ->
                restaurant.filterIds.any { filterId ->
                    filterIds.contains(filterId)
                }
            }

            Result.success(filteredRestaurants)
        } catch(e: Exception) {
            Result.failure(e)
        }
    }
}