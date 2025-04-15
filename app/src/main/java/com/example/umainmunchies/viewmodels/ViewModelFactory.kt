package com.example.umainmunchies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.umainmunchies.repositories.RestaurantRepository

//TODO study this in more detail
class ViewModelFactory(
    private val restaurantRepository: RestaurantRepository,
    private val viewModelStoreOwner: ViewModelStoreOwner
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RestaurantViewModel::class.java) -> {
                RestaurantViewModel(restaurantRepository) as T
            }
            modelClass.isAssignableFrom(FilterViewModel::class.java) -> {
                val restaurantViewModel = ViewModelProvider(
                    viewModelStoreOwner,
                    this
                ).get(RestaurantViewModel::class.java)

                FilterViewModel(restaurantRepository, restaurantViewModel) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}