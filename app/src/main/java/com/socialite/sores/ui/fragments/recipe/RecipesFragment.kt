package com.socialite.sores.ui.fragments.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.sores.R
import com.socialite.sores.adapters.RecipesAdapter
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
class RecipeFragment : Fragment() {

    private val args by navArgs<RecipeFragmentArgs>()

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var mainViewModel: MainViewModel

    private val mAdapter by lazy { RecipesAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel

        setupRecycleView()

        recipesViewModel.readIsBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.isBackOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    recipesViewModel.isNetworkAvailable = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.isNetworkAvailable) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipeBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }

    private fun setupRecycleView() {
        binding.recycleView.adapter = mAdapter
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}