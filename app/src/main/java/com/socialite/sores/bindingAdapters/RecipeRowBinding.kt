package com.socialite.sores.bindingAdapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.socialite.sores.R
import com.socialite.sores.models.Result
import com.socialite.sores.ui.fragments.recipe.RecipeFragmentDirections
import org.jsoup.Jsoup

class RecipeRowBinding {

    companion object {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout: ConstraintLayout, result: Result) {
            recipeRowLayout.setOnClickListener {
                try {
                    val action =
                        RecipeFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e("onRecipeClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, url: String) {
            imageView.load(url) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, isVegan: Boolean) {
            if (isVegan) {
                val color = ContextCompat.getColor(
                    view.context,
                    R.color.green
                )

                when(view) {
                    is TextView -> {
                        view.setTextColor(color)
                    }

                    is ImageView -> {
                       view.setColorFilter(color)
                    }
                }
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, summary: String?) {
            if (!summary.isNullOrEmpty()) {
                val sum = Jsoup.parse(summary).text()
                textView.text = sum
            }
        }

    }

}