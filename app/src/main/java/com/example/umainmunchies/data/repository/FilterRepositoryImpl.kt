package com.example.umainmunchies.data.repository

import com.example.umainmunchies.api.RestaurantApiService
import com.example.umainmunchies.domain.model.FilterEntity
import com.example.umainmunchies.domain.model.toDomain
import com.example.umainmunchies.domain.repository.FilterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterRepositoryImpl(
    private val api: RestaurantApiService
) : FilterRepository {

    override suspend fun getAllFilters(): Result<List<FilterEntity>> = withContext(Dispatchers.IO) {
        try {
            // Since there's no direct endpoint for getting all filters,
            // we'll get all restaurants and extract unique filter IDs
            val response = api.getAllRestaurants()
            val filterIds = response.restaurants.flatMap { it.filterIds }.distinct()

            // Get filter details for each ID
            val filters = mutableListOf<FilterEntity>()
            for (filterId in filterIds) {
                try {
                    val filter = api.getFilter(filterId)
                    filters.add(filter.toDomain())
                } catch (e: Exception) {
                    // Skip this filter if there's an error
                    continue
                }
            }

            Result.success(filters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFilterById(id: String): Result<FilterEntity> = withContext(Dispatchers.IO) {
        try {
            // API returns Filter directly
            val filter = api.getFilter(id)
            Result.success(filter.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFiltersForIds(filterIds: List<String>): Result<List<FilterEntity>> = withContext(Dispatchers.IO) {
        try {
            val filters = mutableListOf<FilterEntity>()

            for (filterId in filterIds) {
                try {
                    val filter = api.getFilter(filterId)
                    filters.add(filter.toDomain())
                } catch (e: Exception) {
                    // Skip this filter if there's an error
                    continue
                }
            }

            Result.success(filters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}