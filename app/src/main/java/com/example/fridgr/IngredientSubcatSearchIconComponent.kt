package com.example.fridgr

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout



class IngredientSubcatSearchIconComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    val SUBCAT_PADDING = 2
    val INGRED_PADDING = 5

    private var iconImageView: ImageView
    private var tickImageView: ImageView
    private var labelTextView: TextView
    private var isSubcat: Boolean = false

    lateinit var iconLabel: String

    var isChecked: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.ingredient_subcat_search_icon_component, this, true)

        this.iconImageView = findViewById(R.id.imgIcon)
        this.labelTextView = findViewById(R.id.txvLabel)
        this.tickImageView = findViewById(R.id.imgTick)

        this.setOnClickListener { onClick() }
    }

    fun setIconProperties(imageResource: Int, labelText: String, isSubcat: Boolean = false) {
        this.iconImageView.setImageResource(imageResource)
        this.labelTextView.text = labelText
        this.iconLabel = labelText
        this.isSubcat = isSubcat

        //Make subcategory icons big and ingredient icons smaller (SUBCAT_PADDING & INGRED_PADDING)
        val scale = resources.displayMetrics.density
        val constraintLayout: ConstraintLayout = this.findViewById(R.id.cnlIngredientSubcatSearchIconComponent)
        if (isSubcat) {
            val padding = (scale * SUBCAT_PADDING + 0.5).toInt()
            constraintLayout.setPadding(padding, padding, padding, padding)
        } else {
            val padding = (scale * INGRED_PADDING + 0.5).toInt()
            constraintLayout.setPadding(padding, padding, padding, padding)
        }
    }

    fun setCheckedState(checkedState: Boolean) {
        this.isChecked = checkedState
        iconImageView.apply {
            if (isSubcat) {
                if (isChecked) {
                    setBackgroundResource(R.drawable.circular_button_checked)
                } else {
                    setBackgroundResource(R.drawable.circular_button_unchecked)
                }
            } else {
                //TODO: Implement tick visual for selecting ingredients to search with (like iOS tick visual)
                if (isChecked) {
                    iconImageView.setColorFilter(Color.argb(127, 0, 0, 255))
                    tickImageView.visibility = View.VISIBLE
                    tickImageView.setColorFilter(Color.argb(127, 41, 255, 163))
                } else {
                    iconImageView.colorFilter = null
                    tickImageView.visibility = View.INVISIBLE
                    tickImageView.colorFilter = null
                }
            }
        }
    }

    fun onClick() {
        //Only handles click events if its an ingredient since we have to delegate subcat
        //click events to the overall controller (IngredientSubcatSearchComponent)
        Log.v("X", "toggling checked state")
        if (!isSubcat) {
            setCheckedState(!this.isChecked)
        }
    }
}