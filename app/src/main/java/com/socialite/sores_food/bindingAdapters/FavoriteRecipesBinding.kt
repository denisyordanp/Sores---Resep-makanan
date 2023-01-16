package com.socialite.sores_food.bindingAdapters

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialite.sores_food.adapters.recycleViews.FavoriteRecipesAdapter
import com.socialite.sores_food.data.database.entities.FavoritesEntity

class FavoriteRecipesBinding {

    companion object {

        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setVisibility(
            view: View,
            favoriteEntities: List<FavoritesEntity>?,
            adapter: FavoriteRecipesAdapter?
        ) {
            when(view) {
                is RecyclerView -> {
                    val isDataNull = favoriteEntities.isNullOrEmpty()
                    view.isInvisible = isDataNull
                    if (!isDataNull) {
                        favoriteEntities?.let {
                            adapter?.setFavorites(it)
                        }
                    }
                }
                else -> {
                    view.isVisible = favoriteEntities.isNullOrEmpty()
                }
            }
        }

    }

}
