package com.socialite.sores.viewModels

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.sores.data.DataStoreRepository
import com.socialite.sores.util.Constants.Companion.API_KEY
import com.socialite.sores.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.socialite.sores.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.socialite.sores.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.socialite.sores.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.socialite.sores.util.Constants.Companion.QUERY_API_KEY
import com.socialite.sores.util.Constants.Companion.QUERY_DIET
import com.socialite.sores.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.socialite.sores.util.Constants.Companion.QUERY_NUMBER
import com.socialite.sores.util.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealTYpe = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var isNetworkAvailable = false

    val readMealAndDietType = dataStoreRepository.readMelaAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealTYpe = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealTYpe
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus() {
        if (!isNetworkAvailable) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }
}