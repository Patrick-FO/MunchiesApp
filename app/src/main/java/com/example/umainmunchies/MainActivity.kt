package com.example.umainmunchies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.umainmunchies.api.RetrofitClient
import com.example.umainmunchies.repositories.RestaurantRepository
import com.example.umainmunchies.ui.theme.UmainMunchiesTheme
import com.example.umainmunchies.viewmodels.FilterViewModel
import com.example.umainmunchies.viewmodels.RestaurantViewModel
import com.example.umainmunchies.views.RestaurantListView
import com.example.umainmunchies.viewmodels.ViewModelFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val apiService = RetrofitClient.restaurantApiService
        val restaurantRepository = RestaurantRepository(apiService)
        val viewModelFactory = ViewModelFactory(restaurantRepository, this)

        setContent {
            UmainMunchiesTheme {
                val restaurantViewModel: RestaurantViewModel = viewModel(factory = viewModelFactory)
                val filterViewModel: FilterViewModel = viewModel(factory = viewModelFactory)

                RestaurantListView(restaurantViewModel = restaurantViewModel, filterViewModel = filterViewModel)
            }
        }
    }
}
