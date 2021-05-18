package com.socialite.sores.adapters.recycleViews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.socialite.sores.R
import com.socialite.sores.databinding.IngredientsRowLayoutBinding
import com.socialite.sores.models.ExtendedIngredient
import com.socialite.sores.util.Constants.Companion.BASE_IMAGE_URL
import com.socialite.sores.util.RecipesDiffUtil

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setTextDataToView(ingredients: ExtendedIngredient) {
            binding.ingredientName.text = ingredients.name
            binding.ingredientAmount.text = ingredients.amount.toString()
            binding.ingredientUnit.text = ingredients.unit
            binding.ingredientConsistency.text = ingredients.consistency
            binding.ingredientOriginal.text = ingredients.original
        }

        fun setImageDataToView(ingredients: ExtendedIngredient) {
            binding.ingredientImageView.load(BASE_IMAGE_URL + ingredients.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IngredientsRowLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
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