package com.socialite.sores_food.data

import com.socialite.sores_food.models.FoodRecipe
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class RemoteDataSourceTest {

    @Test
    fun `response should success`() {
        val dummyResponse = Response.success(
            FoodRecipe(listOf())
        )

        assert(dummyResponse.isSuccessful)
    }

    @Test
    fun `response should error`() {
        val dummyResponse = Response.error<FoodRecipe>(
            400,
            ResponseBody.create(null, "")
        )

        assert(dummyResponse.code() == 400)
    }

}
