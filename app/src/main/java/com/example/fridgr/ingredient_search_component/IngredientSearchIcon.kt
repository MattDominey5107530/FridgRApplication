package com.example.fridgr.ingredient_search_component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import api.Ingredient
import com.example.fridgr.R

@SuppressLint("ViewConstructor")
class IngredientSearchIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): SearchIcon(context, attrs, defStyle) {

    private val iconPadding = 1

    init {
        LayoutInflater.from(context).inflate(R.layout.ingredient_subcat_search_icon_component, this, true)

        this.iconImageView = findViewById(R.id.imgIcon)
        this.labelTextView = findViewById(R.id.txvLabel)
        this.tickImageView = findViewById(R.id.imgTick)
    }

    fun setProperties(ingredient: Ingredient) {
        with(ingredient) {
            setIconProperties(imageString, name)
        }
    }

    fun setCheckedState(checkedState: Boolean) {
        this.isChecked = checkedState
        iconImageView.apply {
            if (isChecked) {
                iconImageView.setColorFilter(Color.argb(127, 250, 250, 250))
                tickImageView.visibility = View.VISIBLE
                tickImageView.setColorFilter(Color.argb(127, 41, 255, 163))
            } else {
                iconImageView.colorFilter = null
                tickImageView.visibility = View.INVISIBLE
                tickImageView.colorFilter = null
            }
        }
    }

    fun onClick() {
        setCheckedState(!isChecked)
    }
}