package com.socialite.sores.data

import com.socialite.sores.data.database.RecipesDao
import com.socialite.sores.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
        private val recipesDao: RecipesDao
) {

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(foodRecipes: RecipesEntity) {
        recipesDao.insertRecipes(foodRecipes)
    }

}