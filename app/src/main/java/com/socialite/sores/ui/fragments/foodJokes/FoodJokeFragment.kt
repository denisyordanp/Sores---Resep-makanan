package com.socialite.sores.ui.fragments.foodJokes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.microsoft.appcenter.analytics.Analytics
import com.socialite.sores.R
import com.socialite.sores.databinding.FragmentFoodJokeBinding
import com.socialite.sores.util.Constants.Companion.API_KEY
import com.socialite.sores.util.NetworkResult
import com.socialite.sores.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()

    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private var foodJoke = "No Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        setViewBinding()
        getFoodJoke()
        return binding.root
    }

    private fun setViewBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.food_joke_share_menu) {
            Analytics.trackEvent("Share Food Joke Button: Clicked")
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFoodJoke() {
        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val data = response.data
                    binding.foodJokeTextView.text = data?.text
                    if (data != null) {
                        foodJoke = data.text
                    }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.d("FoodJokeFragment", "loading")
                }
            }
        }
    }
    
    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner) { database ->
                if (!database.isNullOrEmpty()) {
                    binding.foodJokeTextView.text = database[0].foodJoke.text
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
