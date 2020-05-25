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
                val minString = minCaloriesEditText.text.toString()
                val maxString = maxCaloriesEditText.text.toString()
                if (minString != "" && maxString != "") {
                    IntRange(minString.toInt(), maxString.toInt())
                } else if (minString != "") {
                    IntRange(minString.toInt(), 1000)
                } else if (maxString != "") {
                    IntRange(0, maxString.toInt())
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

        val carbsRange: IntRange? =
        try {
            val minString = minCarbsEditText.text.toString()
            val maxString = maxCarbsEditText.text.toString()
            if (minString != "" && maxString != "") {
                IntRange(minString.toInt(), maxString.toInt())
            } else if (minString != "") {
                IntRange(minString.toInt(), 1000)
            } else if (maxString != "") {
                IntRange(0, maxString.toInt())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

        val fatsRange: IntRange? =
        try {
            val minString = minFatsEditText.text.toString()
            val maxString = maxFatsEditText.text.toString()
            if (minString != "" && maxString != "") {
                IntRange(minString.toInt(), maxString.toInt())
            } else if (minString != "") {
                IntRange(minString.toInt(), 1000)
            } else if (maxString != "") {
                IntRange(0, maxString.toInt())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

        val proteinsRange: IntRange? =
        try {
            val minString = minProteinsEditText.text.toString()
            val maxString = maxProteinsEditText.text.toString()
            if (minString != "" && maxString != "") {
                IntRange(minString.toInt(), maxString.toInt())
            } else if (minString != "") {
                IntRange(minString.toInt(), 1000)
            } else if (maxString != "") {
                IntRange(0, maxString.toInt())
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