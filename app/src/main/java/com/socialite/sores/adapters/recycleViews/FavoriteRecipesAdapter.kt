package com.socialite.sores.adapters.recycleViews

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.socialite.sores.R
import com.socialite.sores.data.database.entities.FavoritesEntity
import com.socialite.sores.databinding.FavoriteRecipesRowLayoutBinding
import com.socialite.sores.ui.fragments.favorites.FavoriteRecipeFragmentDirections
import com.socialite.sores.util.RecipesDiffUtil
import com.socialite.sores.viewModels.MainViewModel
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

class FavoriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelectionMode = false

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var multiSelectedRecipes = arrayListOf<FavoritesEntity>()
    private var favoriteEntities = emptyList<FavoritesEntity>()

    class MyViewHolder(
        private val _binding: FavoriteRecipesRowLayoutBinding,
    ) :
        RecyclerView.ViewHolder(_binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            _binding.favoriteEntity = favoritesEntity
            _binding.executePendingBindings()
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
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentFavorite = favoriteEntities[position]
        holder.bind(currentFavorite)
        setOnSingleClickListener(holder, currentFavorite)
        setOnLongClickListener(holder, currentFavorite)
    }

    private fun setOnSingleClickListener(holder: MyViewHolder, favoritesEntity: FavoritesEntity) {
        holder.itemView.favoriteRecipesRowLayout.setOnClickListener {
            if (multiSelectionMode) {
                applySelection(holder, favoritesEntity)
            } else {
                val action =
                    FavoriteRecipeFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        favoritesEntity.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    private fun setOnLongClickListener(holder: MyViewHolder, favoritesEntity: FavoritesEntity) {
        holder.itemView.favoriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelectionMode) {
                multiSelectionMode = true
                requireActivity.startActionMode(this)
                applySelection(holder, favoritesEntity)
            }
            true
        }
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if (multiSelectedRecipes.contains(currentRecipe)) {
            multiSelectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        } else {
            multiSelectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        }
        applyActionModeTitle()
    }

    private fun applyActionModeTitle() {
        when(val size = multiSelectedRecipes.size) {
            0 -> mActionMode.finish()
            1 -> {
                mActionMode.title = "1 item selected"
            }
            else -> {
                mActionMode.title = "$size items selected"
            }
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.itemView.favorite_row_cardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_favorite_recipe) {
            deleteSelectedFavoriteRecipes(mode)
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        setOffMultiSelectionMode()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun deleteSelectedFavoriteRecipes(actionMode: ActionMode?) {
        multiSelectedRecipes.forEach {
            mainViewModel.deleteFavoriteRecipe(it)
        }
        showDeletedSnack("${multiSelectedRecipes.size} item/s deleted.")
        resetMultiSelections()
        actionMode?.finish()
    }

    private fun setOffMultiSelectionMode() {
        myViewHolders.forEach {
            changeRecipeStyle(it, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        resetMultiSelections()
    }

    private fun resetMultiSelections() {
        multiSelectionMode = false
        multiSelectedRecipes.clear()
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

    private fun showDeletedSnack(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok") {}
            .show()
    }
}