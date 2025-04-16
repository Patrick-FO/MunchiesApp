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
            val response = api.getAllRestaurants()
            val filterIds = response.restaurants.flatMap { it.filterIds }.distinct()

            val filters = mutableListOf<FilterEntity>()
            for (filterId in filterIds) {
                try {
                    val filter = api.getFilter(filterId)
                    filters.add(filter.toDomain())
                } catch (e: Exception) {
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
                    continue
                }
            }

            Result.success(filters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}