package com.socialite.sores.ui.mainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.sores.R
import com.socialite.sores.adapters.RecipesAdapter
import com.socialite.sores.models.FoodRecipe
import com.socialite.sores.util.Constants.Companion.API_KEY
import com.socialite.sores.util.NetworkResult
import com.socialite.sores.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mView: View

    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecycleView()
        requestApiData()

        return mView
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when(response) {
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

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"

        return queries
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