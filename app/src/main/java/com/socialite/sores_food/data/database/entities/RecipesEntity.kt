package com.socialite.sores_food.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.socialite.sores_food.models.FoodRecipe
import com.socialite.sores_food.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
        var foodRecipe: FoodRecipe,
) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

}
