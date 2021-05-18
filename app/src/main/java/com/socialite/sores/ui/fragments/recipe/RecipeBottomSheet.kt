package com.socialite.sores.ui.fragments.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.socialite.sores.databinding.RecipeBottomSheetBinding
import com.socialite.sores.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.socialite.sores.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.socialite.sores.viewModels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RecipeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipesViewModel by viewModels()

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    companion object {
        private val TAG = RecipeBottomSheet::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = RecipeBottomSheetBinding.inflate(inflater, container, false)
        readMealAndDietType()
        setListener()
        return binding.root
    }

    private fun readMealAndDietType() {
        recipeViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, {
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType

            updateChip(it.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(it.selectedDietTypeId, binding.dietTypeChipGroup)
        })
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }
    }

    private fun setListener() {
        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }

        binding.btnRbsApply.setOnClickListener {
            recipeViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            val action =
                RecipeBottomSheetDirections.actionRecipeBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}