package com.socialite.sores.adapters.recycleViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.socialite.sores.R
import com.socialite.sores.models.ExtendedIngredient
import com.socialite.sores.util.Constants.Companion.BASE_IMAGE_URL
import com.socialite.sores.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setTextDataToView(ingredients: ExtendedIngredient) {
            itemView.ingredient_name.text = ingredients.name
            itemView.ingredient_amount.text = ingredients.amount.toString()
            itemView.ingredient_unit.text = ingredients.unit
            itemView.ingredient_consistency.text = ingredients.consistency
            itemView.ingredient_original.text = ingredients.original
        }

        fun setImageDataToView(ingredients: ExtendedIngredient) {
            itemView.ingredient_imageView.load(BASE_IMAGE_URL + ingredients.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredients_row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setImageDataToView(ingredientsList[position])
        holder.setTextDataToView(ingredientsList[position])
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(ingredients: List<ExtendedIngredient>) {
        val ingredientDiffUtil =
            RecipesDiffUtil(ingredientsList, ingredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientDiffUtil)
        ingredientsList = ingredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}