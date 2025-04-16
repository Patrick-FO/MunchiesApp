package com.example.umainmunchies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.umainmunchies.data.state.AppState
import com.example.umainmunchies.domain.usecase.FilterUseCase
import com.example.umainmunchies.domain.usecase.RestaurantUseCase

class ViewModelFactory(
    private val restaurantUseCase: RestaurantUseCase,
    private val filterUseCase: FilterUseCase,
    private val appState: AppState
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RestaurantViewModel::class.java) -> {
                RestaurantViewModel(restaurantUseCase) as T
            }
            modelClass.isAssignableFrom(FilterViewModel::class.java) -> {
                FilterViewModel(filterUseCase, appState) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}