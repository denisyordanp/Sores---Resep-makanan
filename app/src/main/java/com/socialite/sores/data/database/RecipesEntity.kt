package com.socialite.sores.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.socialite.sores.models.FoodRecipe
import com.socialite.sores.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
        var foodRecipe: FoodRecipe,
) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

}