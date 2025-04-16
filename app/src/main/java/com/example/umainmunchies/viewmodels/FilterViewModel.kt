package com.example.umainmunchies.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umainmunchies.data.state.AppState
import com.example.umainmunchies.domain.model.FilterEntity
import com.example.umainmunchies.domain.model.RestaurantEntity
import com.example.umainmunchies.domain.usecase.FilterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FilterViewModel(
    private val filterUseCase: FilterUseCase,
    private val appState: AppState
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _filters = MutableStateFlow<List<FilterEntity>>(emptyList())
    val filters = _filters.asStateFlow()

    private val _filterIdToNameMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val filterIdToNameMap = _filterIdToNameMap.asStateFlow()

    private val _toggledFilterIds = MutableStateFlow<Set<String>>(emptySet())
    val toggledFilterIds = _toggledFilterIds.asStateFlow()

    private val _isListFiltered = MutableStateFlow(false)
    val isListFiltered = _isListFiltered.asStateFlow()

    private val _filtersLoaded = MutableStateFlow(false)
    val filtersLoaded = _filtersLoaded.asStateFlow()

    init {
        Log.d("FilterViewModel", "ViewModel initialized")

        viewModelScope.launch {
            filterUseCase.getActiveFilters().collectLatest { activeFilters ->
                _toggledFilterIds.value = activeFilters
                _isListFiltered.value = activeFilters.isNotEmpty()
            }
        }

        loadAllFilters()
    }

    fun loadAllFilters() {
        viewModelScope.launch {
            if(_filters.value.isNotEmpty()) {
                _filtersLoaded.value = true
                return@launch
            }

            _isLoading.value = true
            _error.value = null

            filterUseCase.getAllFilters()
                .onSuccess { filtersList ->
                    _filters.value = filtersList
                    _filterIdToNameMap.value = filtersList.associate { it.id to it.name }
                    _filtersLoaded.value = true
                    Log.d("FilterViewModel", "Loaded ${filtersList.size} filters")
                }
                .onFailure { error ->
                    _error.value = "Failed to load filters: ${error.message}"
                    Log.e("FilterViewModel", "Error loading filters: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun loadFiltersForRestaurants(restaurants: List<RestaurantEntity>) {
        if (restaurants.isEmpty()) return

        if(_filtersLoaded.value && _filters.value.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            filterUseCase.getFiltersForRestaurants(restaurants)
                .onSuccess { filtersList ->
                    _filters.value = filtersList
                    _filterIdToNameMap.value = filtersList.associate { it.id to it.name }
                    _filtersLoaded.value = true
                    Log.d("FilterViewModel", "Loaded ${filtersList.size} filters")
                }
                .onFailure { error ->
                    _error.value = "Failed to load filters: ${error.message}"
                    Log.e("FilterViewModel", "Error loading filters: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun mapFilterIdToString(filterIds: List<String>): String {
        val currentMap = _filterIdToNameMap.value

        if (currentMap.isEmpty()) {
            return "Loading categories..."
        }

        val availableFilters = filterIds.mapNotNull { filterId ->
            currentMap[filterId]
        }

        return if (availableFilters.isEmpty()) {
            "Loading categories..."
        } else {
            availableFilters.joinToString(" • ")
        }
    }

    fun toggleFilter(filterId: String) {
        viewModelScope.launch {
            filterUseCase.toggleFilter(filterId)
        }
    }

    fun isFilterToggled(filterId: String): Boolean {
        return _toggledFilterIds.value.contains(filterId)
    }
}