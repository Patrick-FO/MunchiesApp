import com.example.umainmunchies.api.RestaurantApiService
import com.example.umainmunchies.domain.model.OpenStatusEntity
import com.example.umainmunchies.domain.model.RestaurantEntity
import com.example.umainmunchies.domain.model.toDomain
import com.example.umainmunchies.domain.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestaurantRepositoryImpl(
    private val api: RestaurantApiService
) : RestaurantRepository {

    override suspend fun getAllRestaurants(): Result<List<RestaurantEntity>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllRestaurants()

            val restaurants = response.restaurants.map { it.toDomain() }
            Result.success(restaurants)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRestaurantById(id: String): Result<RestaurantEntity> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllRestaurants()
            val restaurant = response.restaurants.find { it.id == id }
                ?: return@withContext Result.failure(Exception("Restaurant not found"))

            Result.success(restaurant.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRestaurantOpenStatus(id: String): Result<OpenStatusEntity> = withContext(Dispatchers.IO) {
        try {
            val openStatus = api.getOpenStatus(id)
            Result.success(openStatus.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFilteredRestaurants(filterIds: List<String>): Result<List<RestaurantEntity>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllRestaurants()
            val allRestaurants = response.restaurants.map { it.toDomain() }

            val filteredList = if (filterIds.isEmpty()) {
                allRestaurants
            } else {
                allRestaurants.filter { restaurant ->
                    filterIds.any { filterId ->
                        restaurant.filterIds.contains(filterId)
                    }
                }
            }

            Result.success(filteredList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}