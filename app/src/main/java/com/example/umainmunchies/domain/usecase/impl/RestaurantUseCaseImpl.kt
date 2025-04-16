package com.example.umainmunchies.domain.usecase.impl

import com.example.umainmunchies.data.state.AppState
import com.example.umainmunchies.domain.model.OpenStatusEntity
import com.example.umainmunchies.domain.model.RestaurantEntity
import com.example.umainmunchies.domain.repository.RestaurantRepository
import com.example.umainmunchies.domain.usecase.RestaurantUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestaurantUseCaseImpl(
    private val restaurantRepository: RestaurantRepository,
    private val appState: AppState
) : RestaurantUseCase {

    override suspend fun getAllRestaurants(): Result<List<RestaurantEntity>> {
        return withContext(Dispatchers.IO) {
            restaurantRepository.getAllRestaurants()
        }
    }

    override suspend fun getFilteredRestaurants(filterIds: List<String>): Result<List<RestaurantEntity>> {
        return withContext(Dispatchers.IO) {
            restaurantRepository.getFilteredRestaurants(filterIds)
        }
    }

    override suspend fun getRestaurantOpenStatus(restaurantId: String): Result<OpenStatusEntity> {
        return withContext(Dispatchers.IO) {
            restaurantRepository.getRestaurantOpenStatus(restaurantId)
        }
    }

    override fun formatDeliveryTime(timeMinutes: Float): String {
        return when {
            timeMinutes.toInt() == 1 -> "${timeMinutes.toInt()} min"
            timeMinutes < 60 -> "${timeMinutes.toInt()} mins"
            timeMinutes % 60.0 == 0.0 -> "${(timeMinutes / 60).toInt()}h"
            else -> "${(timeMinutes / 60).toInt()}h ${(timeMinutes % 60).toInt()}min"
        }
    }
}