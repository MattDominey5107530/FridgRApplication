package com.example.fridgr.ingredient_search_component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.fridgr.R

@SuppressLint("ViewConstructor")
class SubcatSearchIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): SearchIcon(context, attrs, defStyle) {

    private val iconPadding = 2

    init {
        LayoutInflater.from(context).inflate(R.layout.ingredient_subcat_search_icon_component, this, true)

        this.iconImageView = findViewById(R.id.imgIcon)
        this.labelTextView = findViewById(R.id.txvLabel)
        this.tickImageView = findViewById(R.id.imgTick)
    }

    fun setProperties(subcatLabel: String,
                      subcatImageResource: Int) {
        setIconProperties(
            ResourcesCompat.getDrawable(context.resources, subcatImageResource, null)!!,
            subcatLabel,
            iconPadding)
    }

    fun setCheckedState(checkedState: Boolean) {
        this.isChecked = checkedState
        iconImageView.apply {
            if (isChecked) {
                setBackgroundResource(R.drawable.circular_button_checked)
            } else {
                setBackgroundResource(R.drawable.circular_button_unchecked)
            }
        }
    }

    fun onClick() {
        setCheckedState(!this.isChecked)
    }
}