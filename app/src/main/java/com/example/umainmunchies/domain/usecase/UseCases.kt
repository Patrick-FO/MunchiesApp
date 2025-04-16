package com.example.umainmunchies.domain.usecase

import com.example.umainmunchies.domain.model.FilterEntity
import com.example.umainmunchies.domain.model.OpenStatusEntity
import com.example.umainmunchies.domain.model.RestaurantEntity
import kotlinx.coroutines.flow.Flow

interface RestaurantUseCase {
    suspend fun getAllRestaurants(): Result<List<RestaurantEntity>>
    suspend fun getFilteredRestaurants(filterIds: List<String>): Result<List<RestaurantEntity>>
    suspend fun getRestaurantOpenStatus(restaurantId: String): Result<OpenStatusEntity>
    fun formatDeliveryTime(timeMinutes: Float): String
}

interface FilterUseCase {
    suspend fun getAllFilters(): Result<List<FilterEntity>>
    suspend fun getFiltersForRestaurants(restaurants: List<RestaurantEntity>): Result<List<FilterEntity>>
    suspend fun getFilterById(filterId: String): Result<FilterEntity>
    fun formatFilterNames(filterIds: List<String>, filtersMap: Map<String, String>): String
    fun getActiveFilters(): Flow<Set<String>>
    suspend fun toggleFilter(filterId: String)
}