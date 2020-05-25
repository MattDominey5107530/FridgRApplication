package com.example.fridgr.popups

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import api.MealType
import api.getMealTypeFromMealTypeString
import api.getMealTypeStringFromMealType
import com.example.fridgr.R
import com.example.fridgr.RecipeSearchFragment

data class Filters(
    val mealType: MealType?,
    val nutritionFilters: NutritionFilters
)

data class NutritionFilters(
    val caloryRange: IntRange?,
    val fatRange: IntRange?,
    val proteinRange: IntRange?,
    val carbRange: IntRange?
)

class FilterPopup(
    context: Context,
    onDismissListener: (AbstractPopup) -> Unit
) :
    AbstractPopup(
        context,
        R.layout.popupwindow_filter, onDismissListener, 300, 475
    ) {

    private val mealTypeSpinner: Spinner = view.findViewById(R.id.spnMealTypes)
    private val minCaloriesEditText: EditText = view.findViewById(R.id.edtxtMinCal)
    private val maxCaloriesEditText: EditText = view.findViewById(R.id.edtxtMaxCal)
    private val minCarbsEditText: EditText = view.findViewById(R.id.edtxtMinCarbs)
    private val maxCarbsEditText: EditText = view.findViewById(R.id.edtxtMaxCarbs)
    private val minFatsEditText: EditText = view.findViewById(R.id.edtxtMinFats)
    private val maxFatsEditText: EditText = view.findViewById(R.id.edtxtMaxFats)
    private val minProteinsEditText: EditText = view.findViewById(R.id.edtxtMinProtein)
    private val maxProteinsEditText: EditText = view.findViewById(R.id.edtxtMaxProtein)

    private val mealTypeSpinnerAdapter: ArrayAdapter<String>

    init {
        val mealTypeStringList: ArrayList<String> =
            ArrayList(MealType.values().map { getMealTypeStringFromMealType(it)!! })
        mealTypeStringList.add(0, "Any")
        mealTypeSpinnerAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            mealTypeStringList
        ).also {
            mealTypeSpinner.adapter = it
        }

        view.findViewById<ImageButton>(R.id.imbBack).setOnClickListener {
            popupWindow.dismiss()
        }
    }

    lateinit var filters: Filters

    override fun fetchFields() {
        val diet =
            getMealTypeFromMealTypeString(mealTypeSpinner.selectedItem as String)

        val caloryRange: IntRange? =
            try {
                val min = minCaloriesEditText.text.toString().toInt()
                val max = maxCaloriesEditText.text.toString().toInt()
                if (min < max) {
                    IntRange(min, max)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

        val carbsRange: IntRange? =
            try {
                val min = minCarbsEditText.text.toString().toInt()
                val max = maxCarbsEditText.text.toString().toInt()
                if (min < max) {
                    IntRange(min, max)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

        val fatsRange: IntRange? =
            try {
                val min = minFatsEditText.text.toString().toInt()
                val max = maxFatsEditText.text.toString().toInt()
                if (min < max) {
                    IntRange(min, max)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

        val proteinsRange: IntRange? =
            try {
                val min = minProteinsEditText.text.toString().toInt()
                val max = maxProteinsEditText.text.toString().toInt()
                if (min < max) {
                    IntRange(min, max)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

        filters = Filters(
            diet,
            NutritionFilters(
                caloryRange,
                fatsRange,
                proteinsRange,
                carbsRange
            )
        )
    }

}