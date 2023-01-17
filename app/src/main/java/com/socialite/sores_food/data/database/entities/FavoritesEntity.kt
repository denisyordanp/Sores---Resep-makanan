package com.socialite.sores_food.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.socialite.sores_food.models.Result
import com.socialite.sores_food.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
) {
    constructor(result: Result) : this(0, result)
}
