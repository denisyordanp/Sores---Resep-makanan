package com.socialite.sores.ui.fragments.recipe

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.microsoft.appcenter.analytics.Analytics
import com.socialite.sores.R
import com.socialite.sores.adapters.recycleViews.RecipesAdapter
import com.socialite.sores.databinding.FragmentRecipesBinding
import com.socialite.sores.models.FoodRecipe
import com.socialite.sores.util.NetworkListener
import com.socialite.sores.util.NetworkResult
import com.socialite.sores.util.observeOnce
import com.socialite.sores.viewModels.MainViewModel
import com.socialite.sores.viewModels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipeFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<RecipeFragmentArgs>()

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val recipesViewModel: RecipesViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val mAdapter by lazy { RecipesAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)

        setViewBinding()
        setupRecycleView()
        setHasOptionsMenu(true)
        setUpViewModel()
        setUpLListener()

        return binding.root
    }

    private fun setViewBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel
    }

    private fun setupRecycleView() {
        binding.recycleView.adapter = mAdapter
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    private fun setUpViewModel() {
        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    recipesViewModel.isNetworkAvailable = status
                    readDatabase()
                }
        }
    }

    private fun setUpLListener() {
        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.isNetworkAvailable) {
                Analytics.trackEvent("Floating Action Button: Clicked")
                findNavController().navigate(R.id.action_recipesFragment_to_recipeBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            Analytics.trackEvent("Search recipes: Submitted", hashMapOf(
                Pair("query", query)
            ))
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setRecipes(database[0].foodRecipe)
                    hideShimmer()
                } else {
                    requestApiData()
                }
            }
        }
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
                    loadDataFromCache()
                    response.message?.let { showToast(it) }
                }
                is NetworkResult.Loading -> {
                    showShimmer()
                }
            }
        }
    }

    private fun searchApiData(searchQuery: String) {
        mainViewModel.searchRecipes(recipesViewModel.applySearchQueries(searchQuery))
        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let { fillAdapter(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    loadDataFromCache()
                    response.message?.let { showToast(it) }
                }
                is NetworkResult.Loading -> {
                    showShimmer()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setRecipes(database[0].foodRecipe)
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

    private fun hideShimmer() {
        binding.recycleView.hideShimmer()
    }

    private fun showShimmer() {
        binding.recycleView.showShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
