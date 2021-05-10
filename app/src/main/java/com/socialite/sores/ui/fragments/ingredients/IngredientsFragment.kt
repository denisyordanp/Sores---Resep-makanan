package com.socialite.sores.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.sores.R
import com.socialite.sores.adapters.recycleViews.IngredientsAdapter
import com.socialite.sores.models.Result
import com.socialite.sores.ui.DetailsActivity
import kotlinx.android.synthetic.main.fragment_ingridients.view.*

class IngredientsFragment : Fragment() {

    private val mAdapter: IngredientsAdapter by lazy {
        IngredientsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingridients, container, false)

        val result = bundle
        setRecycleView(view)
        setIngredients(result)

        return view
    }

    private val bundle: Result?
        get() {
            val args = arguments
            return args?.getParcelable(DetailsActivity.RESULT_BUNDLE_KEY)
        }

    private fun setRecycleView(view: View) {
        view.ingredient_recycleView.adapter = mAdapter
        view.ingredient_recycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setIngredients(result: Result?) {
        result?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
    }
}