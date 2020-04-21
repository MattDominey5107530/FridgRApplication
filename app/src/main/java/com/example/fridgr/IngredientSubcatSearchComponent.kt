package com.example.fridgr

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class IngredientSubcatSearchComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var ingredientSearchEditText: EditText
    private var subcatIconLinearLayout: LinearLayout
    private var ingredientIconGridLayout: GridLayout
    private var ingredientListButton: Button

    private val subcatIcons = arrayListOf<IngredientSubcatSearchIconComponent>()
    private val ingredientIcons = arrayListOf<Pair<String, IngredientSubcatSearchIconComponent>>()

    var checkedIngredientList = ArrayList<String>()

    var searchIsVisible = false

    init {
        LayoutInflater.from(context).inflate(R.layout.ingredient_subcat_search_component, this, true)

        ingredientSearchEditText = findViewById(R.id.edtIngredientSearch)
        subcatIconLinearLayout = findViewById(R.id.lnlSubcatIconContainer)
        ingredientIconGridLayout = findViewById(R.id.gdlIngredientIconContainer)
        ingredientListButton = findViewById(R.id.btnIngredientList)

        ingredientSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { onChangeSearchText() }

            //Redundant but necessary functions to satisfy abstractTextWatcher
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    /**
     * Define and pass values into custom component
     */
    private fun getIngredientSubcatSearchIconComponent(imageResource: Int, labelText: String, isSubcat: Boolean = false): IngredientSubcatSearchIconComponent {
        val ingredientSubcatSearchIconComponent = IngredientSubcatSearchIconComponent(this.context)
        ingredientSubcatSearchIconComponent.setIconProperties(imageResource, labelText, isSubcat)
        return ingredientSubcatSearchIconComponent
    }

    /**
     * Adds a subcategory to the view
     */
    fun addSubcat(labelText: String, imageResource: Int) {
        val iconToAdd = getIngredientSubcatSearchIconComponent(imageResource, labelText, true)
        iconToAdd.setOnClickListener {v -> onClickSubcatIcon(v as IngredientSubcatSearchIconComponent)}

        //Add to view and add to list to manage
        subcatIconLinearLayout.addView(iconToAdd)
        subcatIcons.add(iconToAdd)
    }

    /**
     * Adds an ingredient to a specified subcat in the view
     */
    fun addIngredient(subcatLabel: String, labelText: String, imageResource: Int) {
        val iconToAdd = getIngredientSubcatSearchIconComponent(imageResource, labelText)
        iconToAdd.setOnClickListener {v -> onClickIngredient(v as IngredientSubcatSearchIconComponent) }
        ingredientIcons.add(Pair(subcatLabel, iconToAdd))
    }

    //Update the icon check states according to a new checkedIngredientsList from the PopupWindow
    fun updateCheckedIngredients(newCheckedIngredients: ArrayList<String>) {
        checkedIngredientList = newCheckedIngredients
        for (ingredientIcon in ingredientIcons) {
            ingredientIcon.second.setCheckedState(ingredientIcon.second.iconLabel in checkedIngredientList)
        }
        ingredientListButton.text = checkedIngredientList.size.toString()
    }

    fun setSearchVisibility(setSearchVisible: Boolean) {
        searchIsVisible = setSearchVisible
        if (searchIsVisible) {
            //TODO: Add search icon resource to line of code below (instead of R.drawable.ic_settings)
            val searchIcon = getIngredientSubcatSearchIconComponent(R.drawable.ic_settings, "Search", true)
            ingredientIconGridLayout.addView(searchIcon, 0)
        } else {
            ingredientIconGridLayout.removeViewAt(0)
        }
    }


    private fun onChangeSearchText() {
        //TODO: display search icon in subcat and search for ingredients and set those ingredients in the ingredient gridview (setIngredientIcons())
        //TODO: also, if there is search text, then show search icon if not then don't (setSearchVisibility())
        //text is ingredientSearchEditText.text
        //Gets triggered after every change to the search text
    }

    private fun onClickSubcatIcon(v: IngredientSubcatSearchIconComponent) {
        //Leave only the clicked icon checked and render all ingredients under that subcategory
        uncheckAllSubcatIcons()
        v.setCheckedState(true)
        renderIngredientIcons(v.iconLabel)
    }

    private fun onClickIngredient(v: IngredientSubcatSearchIconComponent) {
        v.onClick()

        if (v.isChecked) {
            checkedIngredientList.add(v.iconLabel)
        } else {
            checkedIngredientList.remove(v.iconLabel)
        }
        ingredientListButton.text = checkedIngredientList.size.toString()
        //TODO: adding this ingredient to the list of ingredients & highlight as ticked
    }

    private fun uncheckAllSubcatIcons() {
        for (i in 1..subcatIconLinearLayout.childCount) {
            val child = subcatIconLinearLayout.getChildAt(i-1) as IngredientSubcatSearchIconComponent
            child.setCheckedState(false)
        }
    }

    private fun renderIngredientIcons(subcatLabel: String) {
        val filteredIconList = ingredientIcons.filter {iI -> iI.first == subcatLabel}
        setIngredientIcons(filteredIconList)
    }

    private fun setIngredientIcons(ingredientIconList: List<Pair<String, IngredientSubcatSearchIconComponent>>) {
        //TODO: if ingredient is in list of ingredients then highlight it
        ingredientIconGridLayout.removeAllViews()
        ingredientIconGridLayout.columnCount = kotlin.math.ceil(ingredientIconList.size.toDouble() / 3.toDouble()).toInt()
        for (iconView in ingredientIconList) {
            ingredientIconGridLayout.addView(iconView.second)
        }
    }
}