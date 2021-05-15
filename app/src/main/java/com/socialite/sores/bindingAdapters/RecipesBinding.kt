package com.socialite.sores.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.socialite.sores.data.database.entities.RecipesEntity
import com.socialite.sores.models.FoodRecipe
import com.socialite.sores.util.NetworkResult

class RecipesBinding {

    companion object {

        @BindingAdapter("imageResponse", "imageDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {

            when{
                apiResponse is NetworkResult.Error && database.isNullOrEmpty() -> {
                    imageView.visibility = View.VISIBLE
                }
                else -> {
                    imageView.visibility = View.INVISIBLE
                }
            }

        }

        @BindingAdapter("textResponse", "textDatabase", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {

            when{
                apiResponse is NetworkResult.Error && database.isNullOrEmpty() -> {
                    textView.visibility = View.VISIBLE
                    textView.text = apiResponse.message.toString()
                }
                else -> {
                    textView.visibility = View.INVISIBLE
                }
            }

        }

    }

}