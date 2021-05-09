package com.socialite.sores.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.socialite.sores.R
import com.socialite.sores.models.Result
import com.socialite.sores.ui.DetailsActivity
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        val result = bundle
        setFragmentView(view, result)
        setVeganType(view, result)

        return view
    }

    private val bundle: Result?
        get() {
            val args = arguments
            return args?.getParcelable(DetailsActivity.RESULT_BUNDLE_KEY)
        }

    private fun setFragmentView(view: View, result: Result?) {
        view.main_overview_imageView.load(result?.image)
        view.title_overview_textView.text = result?.title
        view.likes_overview_textView.text = result?.aggregateLikes.toString()
        view.time_overview_textView.text = result?.readyInMinutes.toString()
        result?.summary.let {
            val summary = Jsoup.parse(it).text()
            view.summary_textView.text = summary
        }
    }

    private fun setVeganType(view: View, result: Result?) {
        if (result?.vegetarian == true) {
            view.vegetarian_overview_imageView.setColorFilter(selectedTypeColor)
            view.vegetarian_overview_textView.setTextColor(selectedTypeColor)
        }

        if (result?.vegan == true) {
            view.vegan_overview_imageView.setColorFilter(selectedTypeColor)
            view.vegan_overview_textView.setTextColor(selectedTypeColor)
        }

        if (result?.glutenFree == true) {
            view.gluten_free_overview_imageView.setColorFilter(selectedTypeColor)
            view.gluten_free_overview_textView.setTextColor(selectedTypeColor)
        }

        if (result?.dairyFree == true) {
            view.dairy_free_overview_imageView.setColorFilter(selectedTypeColor)
            view.dairy_free_overview_textView.setTextColor(selectedTypeColor)
        }

        if (result?.veryHealthy == true) {
            view.healthy_overview_imageView.setColorFilter(selectedTypeColor)
            view.healthy_overview_textView.setTextColor(selectedTypeColor)
        }

        if (result?.cheap == true) {
            view.cheap_overview_imageView.setColorFilter(selectedTypeColor)
            view.cheap_overview_textView.setTextColor(selectedTypeColor)
        }
    }

    private val selectedTypeColor: Int
        get() = ContextCompat.getColor(requireContext(), R.color.green)
}