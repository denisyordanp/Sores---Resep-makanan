package com.socialite.sores_food.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.socialite.sores_food.R
import com.socialite.sores_food.bindingAdapters.RecipeRowBinding
import com.socialite.sores_food.databinding.FragmentOverviewBinding
import com.socialite.sores_food.ui.DetailsActivity
import com.socialite.sores_food.models.Result

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val result = bundle
        setFragmentView(result)
        setVeganType(result)

        return binding.root
    }

    private val bundle: Result
        get() {
            val args = arguments
            return args!!.getParcelable<Result>(DetailsActivity.RESULT_BUNDLE_KEY) as Result
        }

    private fun setFragmentView(result: Result) {
        binding.mainOverviewImageView.load(result.image)
        binding.titleOverviewTextView.text = result.title
        binding.likesOverviewTextView.text = result.aggregateLikes.toString()
        binding.timeOverviewTextView.text = result.readyInMinutes.toString()

        RecipeRowBinding.parseHtml(binding.summaryTextView, result.summary)
    }

    private fun setVeganType(result: Result) {
        updateColors(result.vegetarian, binding.vegetarianOverviewTextView, binding.vegetarianOverviewImageView)
        updateColors(result.vegan, binding.veganOverviewTextView, binding.veganOverviewImageView)
        updateColors(result.glutenFree, binding.glutenFreeOverviewTextView, binding.glutenFreeOverviewImageView)
        updateColors(result.dairyFree, binding.dairyFreeOverviewTextView, binding.dairyFreeOverviewImageView)
        updateColors(result.veryHealthy, binding.healthyOverviewTextView, binding.healthyOverviewImageView)
        updateColors(result.cheap, binding.cheapOverviewTextView, binding.cheapOverviewImageView)
    }

    private fun updateColors(isOn: Boolean, textView: TextView, imageView: ImageView) {
        if (isOn) {
            imageView.setColorFilter(selectedTypeColor)
            textView.setTextColor(selectedTypeColor)
        }
    }

    private val selectedTypeColor: Int
        get() = ContextCompat.getColor(requireContext(), R.color.green)
}
