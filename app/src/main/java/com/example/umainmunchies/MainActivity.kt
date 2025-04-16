package com.example.umainmunchies

import RestaurantRepositoryImpl
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.umainmunchies.api.RetrofitClient
import com.example.umainmunchies.data.repository.FilterRepositoryImpl
import com.example.umainmunchies.data.state.AppState
import com.example.umainmunchies.domain.usecase.impl.FilterUseCaseImpl
import com.example.umainmunchies.domain.usecase.impl.RestaurantUseCaseImpl
import com.example.umainmunchies.ui.theme.UmainMunchiesTheme
import com.example.umainmunchies.viewmodels.FilterViewModel
import com.example.umainmunchies.viewmodels.RestaurantViewModel
import com.example.umainmunchies.viewmodels.ViewModelFactory
import com.example.umainmunchies.views.RestaurantListView

class MainActivity : ComponentActivity() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var filterViewModel: FilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize dependencies
        val apiService = RetrofitClient.restaurantApiService
        val appState = AppState()

        // Create repositories
        val restaurantRepository = RestaurantRepositoryImpl(apiService)
        val filterRepository = FilterRepositoryImpl(apiService)

        // Create use cases
        val restaurantUseCase = RestaurantUseCaseImpl(restaurantRepository, appState)
        val filterUseCase = FilterUseCaseImpl(filterRepository, appState)

        // Create ViewModels
        val factory = ViewModelFactory(restaurantUseCase, filterUseCase, appState)
        restaurantViewModel = ViewModelProvider(this, factory)[RestaurantViewModel::class.java]
        filterViewModel = ViewModelProvider(this, factory)[FilterViewModel::class.java]

        setContent {
            UmainMunchiesTheme {
                RestaurantListView(
                    restaurantViewModel = restaurantViewModel,
                    filterViewModel = filterViewModel
                )
            }
        }
    }
}