package com.socialite.sores.adapters.recycleViews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.sores.data.database.entities.FavoritesEntity
import com.socialite.sores.databinding.FavoriteRecipesRowLayoutBinding
import com.socialite.sores.models.Result
import com.socialite.sores.ui.fragments.favorites.FavoriteRecipeFragmentDirections
import com.socialite.sores.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

class FavoriteRecipesAdapter : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>() {

    private var favoriteEntities = emptyList<FavoritesEntity>()

    class MyViewHolder(private val _binding: FavoriteRecipesRowLayoutBinding):
        RecyclerView.ViewHolder(_binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            _binding.favoriteEntity = favoritesEntity
            _binding.executePendingBindings()
        }

        fun setOnSingleClickListener(result: Result) {
            itemView.favoriteRecipesRowLayout.setOnClickListener {
                val action =
                    FavoriteRecipeFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(result)
                itemView.findNavController().navigate(action)
            }
        }

        companion object {

            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val favoritesEntity = favoriteEntities[position]
        holder.bind(favoritesEntity)
        holder.setOnSingleClickListener(favoritesEntity.result)
    }

    override fun getItemCount(): Int {
        return favoriteEntities.size
    }

    fun setFavorites(favoritesEntities: List<FavoritesEntity>) {
        val favoriteRecipeDiffUtils = RecipesDiffUtil(this.favoriteEntities, favoritesEntities)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipeDiffUtils)
        this.favoriteEntities = favoritesEntities
        diffUtilResult.dispatchUpdatesTo(this)
    }
}