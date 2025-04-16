package com.example.umainmunchies.data.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppState {
    private val _activeFilterIds = MutableStateFlow<Set<String>>(emptySet())
    val activeFilterIds: StateFlow<Set<String>> = _activeFilterIds.asStateFlow()

    private val _filterIdToNameMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val filterIdToNameMap: StateFlow<Map<String, String>> = _filterIdToNameMap.asStateFlow()

    private val _filtersLoaded = MutableStateFlow(false)
    val filtersLoaded: StateFlow<Boolean> = _filtersLoaded.asStateFlow()

    private val _isFilterActive = MutableStateFlow(false)
    val isFilterActive: StateFlow<Boolean> = _isFilterActive.asStateFlow()

    fun updateActiveFilters(filterIds: Set<String>) {
        _activeFilterIds.value = filterIds
        _isFilterActive.value = filterIds.isNotEmpty()
    }

    fun toggleFilter(filterId: String) {
        val currentFilters = _activeFilterIds.value.toMutableSet()

        if (currentFilters.contains(filterId)) {
            currentFilters.remove(filterId)
        } else {
            currentFilters.add(filterId)
        }

        updateActiveFilters(currentFilters)
    }

    fun updateFilterMap(filterMap: Map<String, String>) {
        _filterIdToNameMap.value = filterMap
        _filtersLoaded.value = true
    }

    fun getFilterNameById(filterId: String): String {
        return _filterIdToNameMap.value[filterId] ?: "Loading..."
    }

    fun formatFilterNames(filterIds: List<String>): String {
        if (_filterIdToNameMap.value.isEmpty()) {
            return "Loading categories..."
        }

        return filterIds
            .mapNotNull { _filterIdToNameMap.value[it] }
            .takeIf { it.isNotEmpty() }
            ?.joinToString(" • ")
            ?: "Loading categories..."
    }
}