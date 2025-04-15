package com.example.umainmunchies.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umainmunchies.api.Filter
import com.example.umainmunchies.repositories.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import okhttp3.internal.toImmutableMap

class FilterViewModel(
    private val restaurantRepository: RestaurantRepository,
    private val restaurantViewModel: RestaurantViewModel
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _filterIdList = MutableStateFlow<List<String>?>(null)
    val filterIdList = _filterIdList.asStateFlow()

    private var _filterIdMap = MutableStateFlow<Map<String, String>?>(null)
    val filterIdMap = _filterIdMap.asStateFlow()

    private val _filterObjectList = MutableStateFlow<MutableList<Filter>?>(mutableListOf())
    val filterObjectList = _filterObjectList.asStateFlow()

    private val _toggledFilterIds = MutableStateFlow<Set<String>>(setOf())
    val toggledFilterIds = _toggledFilterIds.asStateFlow()

    private val _isListFiltered = MutableStateFlow<Boolean>(false)
    val isListFiltered = _isListFiltered.asStateFlow()

    init {
        Log.d("Filter view model", "ViewModel initialized")
        viewModelScope.launch(Dispatchers.IO) {
            restaurantViewModel.restaurants.collect { restaurants ->
                if (restaurants.isNotEmpty()) {
                    collectFilterIds()
                }
            }
        }
    }

    private fun collectFilterIds() {
        val currentList = mutableListOf<String>()

        for(restaurant in restaurantViewModel.restaurants.value) {
            for(filterId in restaurant.filterIds) {
                if(!currentList.contains(filterId)) {
                    currentList.add(filterId)
                }
            }
        }

        Log.d("FilterViewModel", "Collected ${currentList.size} filter IDs: $currentList")
        _filterIdList.value = currentList.toImmutableList()
        loadFilterDetails(currentList)
        mapValuesAndFilterIds(currentList)
    }

    fun mapValuesAndFilterIds(filterList: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val newMap = mutableMapOf<String, String>()

            for (filterId in filterList) {
                restaurantRepository.getFilter(filterId)
                    .onSuccess { filter ->
                        newMap[filterId] = filter.name
                        Log.d("Filter IDs and names map", "Map: $newMap")
                    }
                    .onFailure {
                        _error.value = "Error fetching filter: ${it.message}"
                        Log.e("Filter IDs and names map", "Error fetching map")
                    }
            }
            _filterIdMap.value = newMap.toImmutableMap()
        }
    }

    fun loadFilterDetails(localFilterIdList: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            val newFilterList = mutableListOf<Filter>()

            for(filterId in localFilterIdList){
                restaurantRepository.getFilter(filterId)
                    .onSuccess { filter ->
                        newFilterList.add(filter)
                        Log.d("Filter object", "Filter object: $newFilterList")
                    }
                    .onFailure {
                        _error.value = "Error fetching filter details: ${it.message}"
                        Log.e("Filter object", "Error fetching filter details: $newFilterList")
                    }
            }
            _filterObjectList.value = newFilterList
            _isLoading.value = false
        }
    }

    fun mapFilterIdToString(currentFilterIdList: List<String>): String {
        val idMap = filterIdMap.value
        if(idMap == null || idMap.isEmpty()) {
            return "Loading categories..."
        }

        val currentList = mutableListOf<String>()
        for(i in currentFilterIdList) {
            idMap[i]?.let { name ->
                currentList.add(name)
            } ?: currentList.add("Unknown")
        }
        return currentList.joinToString(" • ")
    }

    fun toggleFilter(filterId: String) {
        val currentToggledSet = _toggledFilterIds.value.toMutableSet()
        if(currentToggledSet.contains(filterId)) {
            currentToggledSet.remove(filterId)
        } else {
            currentToggledSet.add(filterId)
        }
        _toggledFilterIds.value = currentToggledSet

        _isListFiltered.value = _toggledFilterIds.value.isNotEmpty()

        applyFilters()
    }

    private fun applyFilters() {
        val activeFilters = _toggledFilterIds.value

        if(activeFilters.isEmpty()) {
            restaurantViewModel.loadAllRestaurants()
        } else {
            restaurantViewModel.applyFilters(activeFilters.toList())
        }
    }

    fun isFilterToggled(filterId: String): Boolean {
        return _toggledFilterIds.value.contains(filterId)
    }
}