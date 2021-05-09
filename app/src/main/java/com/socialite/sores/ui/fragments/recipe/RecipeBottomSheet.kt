package com.socialite.sores.ui.fragments.recipe

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.socialite.sores.R
import com.socialite.sores.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.socialite.sores.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.socialite.sores.viewModels.RecipesViewModel
import kotlinx.android.synthetic.main.recipe_bottom_sheet.view.*
import java.util.*

class RecipeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipeViewModel: RecipesViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    companion object {
        private val TAG = RecipeBottomSheet::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val mView = inflater.inflate(R.layout.recipe_bottom_sheet, container, false)
        readMealAndDietType(mView)
        setListener(mView)
        return mView
    }

    private fun readMealAndDietType(view: View) {
        recipeViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, {
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType

            updateChip(it.selectedMealTypeId, view.mealTYpe_chipGroup)
            updateChip(it.selectedDietTypeId, view.dietType_chipGroup)
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

    private fun setListener(view: View) {
        view.mealTYpe_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        view.dietType_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }

        view.btn_rbs_apply.setOnClickListener {
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
}