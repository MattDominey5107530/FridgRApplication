package com.example.fridgr

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class IngredientSubcatSearchComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var ingredientSearchEditText: EditText
    private var subcatIconLinearLayout: LinearLayout
    private var ingredientIconLinearLayout: LinearLayout

    private val subcatIcons = arrayListOf<IngredientSubcatSearchIconComponent>()
    private val ingredientIcons = arrayListOf<Pair<String, IngredientSubcatSearchIconComponent>>()

    var searchIsVisible = false

    init {
        LayoutInflater.from(context).inflate(R.layout.ingredient_subcat_search_component, this, true)

        ingredientSearchEditText = findViewById(R.id.edtIngredientSearch)
        subcatIconLinearLayout = findViewById(R.id.lnlSubcatIconContainer)
        ingredientIconLinearLayout = findViewById(R.id.lnlIngredientIconContainer)

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
    private fun getIngredientSubcatSearchIconComponent(
        imageResource: Int, labelText: String
    ): IngredientSubcatSearchIconComponent {
        val ingredientSubcatSearchIconComponent = IngredientSubcatSearchIconComponent(this.context)
        ingredientSubcatSearchIconComponent.setIconProperties(imageResource, labelText)

        return ingredientSubcatSearchIconComponent
    }

    /**
     * Adds a subcategory to the view
     */
    fun addSubcat(labelText: String, imageResource: Int) {

        val iconToAdd = getIngredientSubcatSearchIconComponent(imageResource, labelText)
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
        iconToAdd.setOnClickListener {v -> onClickIngredient(v as IngredientSubcatSearchIconComponent)}

        ingredientIcons.add(Pair(subcatLabel, iconToAdd))
    }

    fun toggleSearchVisibility() {
        searchIsVisible = !searchIsVisible
        if (searchIsVisible) {
            //TODO: Add search icon resource to line of code below (instead of R.drawable.ic_settings)
            val searchIcon = getIngredientSubcatSearchIconComponent(R.drawable.ic_settings, "Search")
            ingredientIconLinearLayout.addView(searchIcon, 0)
        } else {
            ingredientIconLinearLayout.removeViewAt(0)
        }



    }


    private fun onChangeSearchText() {
        //TODO: display search icon in subcat and search for ingredients
        //Gets triggered after every change to the search text
    }

    private fun onClickSubcatIcon(v: IngredientSubcatSearchIconComponent) {
        //Leave only the clicked icon checked and render all ingredients under that subcategory
        uncheckAllSubcatIcons()
        v.setCheckedState(true)
        renderIngredientIcons(v.iconLabel)
    }

    private fun onClickIngredient(v: IngredientSubcatSearchIconComponent) {
        //TODO: handle passing that information to the next view (i.e. Do you want to add this ingredient to the list)
    }

    private fun uncheckAllSubcatIcons() {
        for (i in 1..subcatIconLinearLayout.childCount) {
            val child = subcatIconLinearLayout.getChildAt(i-1) as IngredientSubcatSearchIconComponent
            child.setCheckedState(false)
        }
    }

    private fun renderIngredientIcons(subcatLabel: String) {
        ingredientIconLinearLayout.removeAllViews()
        val filteredIconList = ingredientIcons.filter {iI -> iI.first == subcatLabel}
        for (iconView in filteredIconList) {
            ingredientIconLinearLayout.addView(iconView.second)
        }
    }

}