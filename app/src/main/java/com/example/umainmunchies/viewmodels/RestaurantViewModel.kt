package com.example.umainmunchies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umainmunchies.api.Filter
import com.example.umainmunchies.api.OpenStatus
import com.example.umainmunchies.api.Restaurant
import com.example.umainmunchies.repositories.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel (
    private val restaurantRepository: RestaurantRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants = _restaurants.asStateFlow()

    private val _filters = MutableStateFlow<List<Filter>>(emptyList())
    val filters = _filters.asStateFlow()

    private val _selectedFilterId = MutableStateFlow<List<Filter>?>(null)
    val selectedFilterId = _selectedFilterId.asStateFlow()

    private val _openStatus = MutableStateFlow<OpenStatus?>(null)
    val openStatus = _openStatus.asStateFlow()

    private val _selectedRestaurant = MutableStateFlow<Restaurant?>(null)
    val selectedRestaurant = _selectedRestaurant.asStateFlow()

    init {
        loadAllRestaurants()
    }

    fun loadAllRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            _selectedFilterId.value = null

            restaurantRepository.getAllRestaurants()
                .onSuccess {
                    _restaurants.value = it
                }
                .onFailure {
                    _error.value = "Failed to load restaurants: ${it.message}"
                }

            _isLoading.value = false
        }
    }

    fun applyFilter(filterId: List<Filter>) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _selectedFilterId.value = filterId

            restaurantRepository.getRestaurantsByFilterId(filterId)
                .onSuccess {
                    _restaurants.value = it
                }
                .onFailure {
                    _error.value = "Failed to apply filter: ${it.message}"
                }
            _isLoading.value = false
        }
    }

    //TODO SPARE HOTSWAP FILTERING FUNCTION
    /*fun applyFilters(filterIds: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true

            val originalList = restaurantRepository.getAllRestaurants()
                .getOrDefault(emptyList())

            val filteredList = if(filterIds.isEmpty()) {
                    originalList
            } else {
                originalList.filter { restaurant ->
                    restaurant.filterIds.any { it in filterIds }
                }
            }

            _restaurants.value = filteredList

            _isLoading.value = false
        }
    }*/

    fun applyFilters(filterIds: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true

            val allRestaurants = restaurantRepository.getAllRestaurants().getOrDefault(emptyList())

            if (filterIds.isEmpty()) {
                _restaurants.value = allRestaurants
            } else {
                val filteredList = allRestaurants.filter { restaurant ->
                    filterIds.all { filterId ->
                        restaurant.filterIds.contains(filterId)
                    }
                }
                _restaurants.value = filteredList
            }

            _isLoading.value = false
        }
    }

    fun setSelectedRestaurant(restaurant: Restaurant?) {
        _selectedRestaurant.value = restaurant

        restaurant?.let {
            getOpenStatus(it.id)
        }
    }

    fun getOpenStatus(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            restaurantRepository.getOpenStatus(restaurantId)
                .onSuccess {
                    _openStatus.value = it
                }
                .onFailure {
                    _error.value = "Failed to get open status: ${it.message}"
                }

            _isLoading.value = false
        }
    }

    fun formatTime(time: Float): String {
        if(time.toInt() == 1) {
            return "${time.toInt()} min"
        } else if(time < 60) {
            return "${time.toInt()} mins"
        } else if(time % 60.0 == 0.0) {
            return "${(time / 60).toInt()}h"
        } else {
            return "${time / 60}h"
        }
    }
}