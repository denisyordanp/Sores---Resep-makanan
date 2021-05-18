package com.socialite.sores.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialite.sores.adapters.recycleViews.FavoriteRecipesAdapter
import com.socialite.sores.data.database.entities.FavoritesEntity

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