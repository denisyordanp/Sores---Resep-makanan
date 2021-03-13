package com.socialite.sores.data.network

import com.socialite.sores.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {
    @GET("/recipes/complexSearch")
    suspend fun getRecipe(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>
}