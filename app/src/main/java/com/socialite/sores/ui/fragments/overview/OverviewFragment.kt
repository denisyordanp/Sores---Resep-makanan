package com.socialite.sores.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.socialite.sores.R
import com.socialite.sores.databinding.FragmentOverviewBinding
import com.socialite.sores.models.Result
import com.socialite.sores.ui.DetailsActivity
import org.jsoup.Jsoup

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

    private val bundle: Result?
        get() {
            val args = arguments
            return args?.getParcelable(DetailsActivity.RESULT_BUNDLE_KEY)
        }

    private fun setFragmentView(result: Result?) {
        binding.mainOverviewImageView.load(result?.image)
        binding.titleOverviewTextView.text = result?.title
        binding.likesOverviewTextView.text = result?.aggregateLikes.toString()
        binding.timeOverviewTextView.text = result?.readyInMinutes.toString()
        result?.summary.let {
            val summary = Jsoup.parse(it).text()
            binding.summaryTextView.text = summary
        }
    }

    private fun setVeganType(result: Result?) {
        if (result?.vegetarian == true) {
            binding.vegetarianOverviewImageView.setColorFilter(selectedTypeColor)
            binding.vegetarianOverviewTextView.setTextColor(selectedTypeColor)
        }

        if (result?.vegan == true) {
            binding.veganOverviewImageView.setColorFilter(selectedTypeColor)
            binding.veganOverviewTextView.setTextColor(selectedTypeColor)
        }

        if (result?.glutenFree == true) {
            binding.glutenFreeOverviewImageView.setColorFilter(selectedTypeColor)
            binding.glutenFreeOverviewTextView.setTextColor(selectedTypeColor)
        }

        if (result?.dairyFree == true) {
            binding.dairyFreeOverviewImageView.setColorFilter(selectedTypeColor)
            binding.dairyFreeOverviewTextView.setTextColor(selectedTypeColor)
        }

        if (result?.veryHealthy == true) {
            binding.healthyOverviewImageView.setColorFilter(selectedTypeColor)
            binding.healthyOverviewTextView.setTextColor(selectedTypeColor)
        }

        if (result?.cheap == true) {
            binding.cheapOverviewImageView.setColorFilter(selectedTypeColor)
            binding.cheapOverviewTextView.setTextColor(selectedTypeColor)
        }
    }

    private val selectedTypeColor: Int
        get() = ContextCompat.getColor(requireContext(), R.color.green)
}