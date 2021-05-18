package com.socialite.sores.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.socialite.sores.R
import com.socialite.sores.adapters.recycleViews.FavoriteRecipesAdapter
import com.socialite.sores.databinding.FragmentFavoriteRecipesBinding
import com.socialite.sores.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipeFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        setViewBinding()
        setHasOptionsMenu(true)
        setRecycleView(binding.favoriteRecipesRecycleView)
        return binding.root
    }

    private fun setViewBinding() {
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.adapter = mAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipe_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all_favorite_recipe) {
            mainViewModel.deleteAllFavoriteRecipes()
            showDeletedAllFavoriteRecipes()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setRecycleView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showDeletedAllFavoriteRecipes() {
        Snackbar.make(
            binding.root,
            "All favorite recipes deleted.",
            Snackbar.LENGTH_SHORT
        ).setAction("Ok") {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}