package com.socialite.sores.adapters.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.socialite.sores.R

class RecipeRowBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, url: String) {
            imageView.load(url) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("setReadyInMinutes")
        @JvmStatic
        fun setReadyInMinutes(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()
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

    }

}