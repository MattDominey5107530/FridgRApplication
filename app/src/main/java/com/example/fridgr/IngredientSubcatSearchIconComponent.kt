package com.example.fridgr

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class IngredientSubcatSearchIconComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var iconImageView: ImageView
    private var labelTextView: TextView

    lateinit var iconLabel: String

    var isChecked: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.ingredient_subcat_search_icon_component, this, true)

        iconImageView = findViewById(R.id.imgIcon)
        labelTextView = findViewById(R.id.txvLabel)
    }

    fun setIconProperties(imageResource: Int, labelText: String) {
        iconImageView.setImageResource(imageResource)
        labelTextView.text = labelText
        iconLabel = labelText
    }

    fun setCheckedState(checkedState: Boolean) {
        isChecked = checkedState
        iconImageView.apply {
            if (isChecked) {
                setBackgroundResource(R.drawable.circular_button_checked)
            } else {
                setBackgroundResource(R.drawable.circular_button_unchecked)
            }
        }
    }



}