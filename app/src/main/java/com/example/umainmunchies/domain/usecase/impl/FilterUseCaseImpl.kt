package com.example.umainmunchies.domain.usecase.impl

import com.example.umainmunchies.data.state.AppState
import com.example.umainmunchies.domain.model.FilterEntity
import com.example.umainmunchies.domain.model.RestaurantEntity
import com.example.umainmunchies.domain.repository.FilterRepository
import com.example.umainmunchies.domain.usecase.FilterUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterUseCaseImpl(
    private val filterRepository: FilterRepository,
    private val appState: AppState
) : FilterUseCase {

    private var filterCache = mutableMapOf<String, FilterEntity>()
    private var hasLoadedAllFilters = false

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getAllFilters()
        }
    }

    override suspend fun getAllFilters(): Result<List<FilterEntity>> {
        return withContext(Dispatchers.IO) {
            if (hasLoadedAllFilters && filterCache.isNotEmpty()) {
                return@withContext Result.success(filterCache.values.toList())
            }

            filterRepository.getAllFilters().also { result ->
                result.onSuccess { filters ->
                    filters.forEach { filter ->
                        filterCache[filter.id] = filter
                    }

                    hasLoadedAllFilters = true

                    val filterMap = filters.associate { it.id to it.name }
                    appState.updateFilterMap(filterMap)
                }
            }
        }
    }

    override suspend fun getFiltersForRestaurants(restaurants: List<RestaurantEntity>): Result<List<FilterEntity>> {
        return withContext(Dispatchers.IO) {
            if (hasLoadedAllFilters) {
                val filterIds = restaurants.flatMap { it.filterIds }.toSet()
                val cachedFilters = filterIds.mapNotNull { filterCache[it] }

                if (cachedFilters.size == filterIds.size) {
                    return@withContext Result.success(cachedFilters)
                }
            }

            val filterIds = restaurants.flatMap { it.filterIds }.toSet().toList()

            filterRepository.getFiltersForIds(filterIds).also { result ->
                result.onSuccess { filters ->
                    filters.forEach { filter ->
                        filterCache[filter.id] = filter
                    }

                    val filterMap = filters.associate { it.id to it.name }
                    appState.updateFilterMap(filterMap)
                }
            }
        }
    }

    override suspend fun getFilterById(filterId: String): Result<FilterEntity> {
        return withContext(Dispatchers.IO) {
            filterCache[filterId]?.let {
                return@withContext Result.success(it)
            }

            filterRepository.getFilterById(filterId).also { result ->
                result.onSuccess { filter ->
                    filterCache[filter.id] = filter
                }
            }
        }
    }

    override fun formatFilterNames(filterIds: List<String>, filtersMap: Map<String, String>): String {
        if (filtersMap.isEmpty()) {
            return "Loading categories..."
        }

        return filterIds
            .mapNotNull { filtersMap[it] }
            .takeIf { it.isNotEmpty() }
            ?.joinToString(" • ")
            ?: "Loading categories..."
    }

    override fun getActiveFilters(): Flow<Set<String>> {
        return appState.activeFilterIds
    }

    override suspend fun toggleFilter(filterId: String) {
        appState.toggleFilter(filterId)
    }
}