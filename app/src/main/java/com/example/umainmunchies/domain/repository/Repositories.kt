package com.example.umainmunchies.domain.repository

import com.example.umainmunchies.domain.model.FilterEntity
import com.example.umainmunchies.domain.model.OpenStatusEntity
import com.example.umainmunchies.domain.model.RestaurantEntity

interface RestaurantRepository {
    suspend fun getAllRestaurants(): Result<List<RestaurantEntity>>
    suspend fun getRestaurantById(id: String): Result<RestaurantEntity>
    suspend fun getRestaurantOpenStatus(id: String): Result<OpenStatusEntity>
    suspend fun getFilteredRestaurants(filterIds: List<String>): Result<List<RestaurantEntity>>
}

interface FilterRepository {
    suspend fun getAllFilters(): Result<List<FilterEntity>>
    suspend fun getFilterById(id: String): Result<FilterEntity>
    suspend fun getFiltersForIds(filterIds: List<String>): Result<List<FilterEntity>>
}