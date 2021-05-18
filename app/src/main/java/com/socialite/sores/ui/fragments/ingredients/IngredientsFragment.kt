package com.socialite.sores.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.sores.adapters.recycleViews.IngredientsAdapter
import com.socialite.sores.databinding.FragmentIngridientsBinding
import com.socialite.sores.models.Result
import com.socialite.sores.ui.DetailsActivity

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngridientsBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: IngredientsAdapter by lazy {
        IngredientsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngridientsBinding.inflate(inflater, container, false)

        val result = bundle
        setRecycleView()
        setIngredients(result)

        return binding.root
    }

    private val bundle: Result?
        get() {
            val args = arguments
            return args?.getParcelable(DetailsActivity.RESULT_BUNDLE_KEY)
        }

    private fun setRecycleView() {
        binding.ingredientRecycleView.adapter = mAdapter
        binding.ingredientRecycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setIngredients(result: Result?) {
        result?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}