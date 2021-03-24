package com.socialite.sores.ui.mainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.sores.R
import com.socialite.sores.adapters.RecipesAdapter
import com.socialite.sores.models.FoodRecipe
import com.socialite.sores.util.NetworkResult
import com.socialite.sores.viewModels.MainViewModel
import com.socialite.sores.viewModels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mView: View

    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)

        setupRecycleView()
        requestApiData()

        return mView
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let { fillAdapter(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    response.message?.let { showToast(it) }
                }
                is NetworkResult.Loading -> {
                    showShimmer()
                }
            }
        }
    }

    private fun fillAdapter(recipe: FoodRecipe) {
        mAdapter.setRecipes(recipe)
    }

    private fun showToast(message: String) {
        Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupRecycleView() {
        mView.recycleView.adapter = mAdapter
        mView.recycleView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    private fun hideShimmer() {
        mView.recycleView?.hideShimmer()
    }

    private fun showShimmer() {
        mView.recycleView?.showShimmer()
    }

}