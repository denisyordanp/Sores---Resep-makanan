package com.socialite.sores.data.network

import com.socialite.sores.models.FoodJoke
import com.socialite.sores.models.FoodRecipe
import com.socialite.sores.util.Constants.Companion.SEARCH_API
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET(SEARCH_API)
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET(SEARCH_API)
    suspend fun searchRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey: String
    ): Response<FoodJoke>
}