package com.socialite.sores.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.sores.databinding.RecipesRowLayoutBinding
import com.socialite.sores.models.FoodRecipe
import com.socialite.sores.models.Result
import com.socialite.sores.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()

    class MyViewHolder(private val _binding: RecipesRowLayoutBinding):
        RecyclerView.ViewHolder(_binding.root) {

        fun bind(result: Result) {
            _binding.result = result
            _binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setRecipes(recipes: FoodRecipe) {
        val recipesDiffUtils = RecipesDiffUtil(this.recipes, recipes.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtils)
        this.recipes = recipes.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}