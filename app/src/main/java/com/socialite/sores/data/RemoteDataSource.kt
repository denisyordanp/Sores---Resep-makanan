package com.socialite.sores.data

import com.socialite.sores.data.network.FoodRecipesApi
import com.socialite.sores.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipes: FoodRecipesApi
){
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipes.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return foodRecipes.searchRecipes(searchQuery)
    }
}