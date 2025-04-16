package com.example.umainmunchies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umainmunchies.domain.model.OpenStatusEntity
import com.example.umainmunchies.domain.model.RestaurantEntity
import com.example.umainmunchies.domain.usecase.RestaurantUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel(
    private val restaurantUseCase: RestaurantUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _restaurants = MutableStateFlow<List<RestaurantEntity>>(emptyList())
    val restaurants: StateFlow<List<RestaurantEntity>> = _restaurants.asStateFlow()

    private val _selectedRestaurant = MutableStateFlow<RestaurantEntity?>(null)
    val selectedRestaurant = _selectedRestaurant.asStateFlow()

    private val _openStatus = MutableStateFlow<OpenStatusEntity?>(null)
    val openStatus = _openStatus.asStateFlow()

    init {
        loadAllRestaurants()
    }

    fun loadAllRestaurants() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            restaurantUseCase.getAllRestaurants()
                .onSuccess { restaurants ->
                    _restaurants.value = restaurants
                }
                .onFailure { error ->
                    _error.value = "Failed to load restaurants: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun applyFilters(filterIds: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            restaurantUseCase.getFilteredRestaurants(filterIds)
                .onSuccess { filteredRestaurants ->
                    _restaurants.value = filteredRestaurants
                }
                .onFailure { error ->
                    _error.value = "Failed to apply filters: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun setSelectedRestaurant(restaurant: RestaurantEntity?) {
        _selectedRestaurant.value = restaurant

        restaurant?.let {
            getOpenStatus(it.id)
        }
    }

    private fun getOpenStatus(restaurantId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            restaurantUseCase.getRestaurantOpenStatus(restaurantId)
                .onSuccess { status ->
                    _openStatus.value = status
                }
                .onFailure { error ->
                    _error.value = "Failed to get open status: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun formatDeliveryTime(timeMinutes: Float): String {
        return restaurantUseCase.formatDeliveryTime(timeMinutes)
    }
}