package com.socialite.sores_food.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.socialite.sores_food.data.DataStoreRepository
import com.socialite.sores_food.util.Constants.Companion.API_KEY
import com.socialite.sores_food.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.socialite.sores_food.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.socialite.sores_food.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.socialite.sores_food.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.socialite.sores_food.util.Constants.Companion.QUERY_API_KEY
import com.socialite.sores_food.util.Constants.Companion.QUERY_DIET
import com.socialite.sores_food.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.socialite.sores_food.util.Constants.Companion.QUERY_NUMBER
import com.socialite.sores_food.util.Constants.Companion.QUERY_SEARCH
import com.socialite.sores_food.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealTYpe = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var isNetworkAvailable = false
    var isBackOnline = false

    val readMealAndDietType = dataStoreRepository.readMelaAndDietType
    val readIsBackOnline = dataStoreRepository.readIsBackOnline.asLiveData()

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    private fun saveIsBackOnline(isBackOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveIsBackOnline(isBackOnline)
        }

    fun applyQueries(): HashMap<String, String> {
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealTYpe = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        val queries = baseQuery
        queries[QUERY_TYPE] = mealTYpe
        queries[QUERY_DIET] = dietType

        return queries
    }

    fun applySearchQueries(searchQuery: String): HashMap<String, String> {

        val queries = baseQuery
        queries[QUERY_SEARCH] = searchQuery

        return queries
    }

    private val baseQuery: HashMap<String, String>
    get() {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun showNetworkStatus() {
        if (!isNetworkAvailable) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveIsBackOnline(true)
        } else {
            if (isBackOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveIsBackOnline(false)
            }
        }
    }
}
