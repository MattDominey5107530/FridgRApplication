package com.example.fridgr.ingredient_search_component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fridgr.R

open class SearchIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): ConstraintLayout(context, attrs, defStyle) {

    protected lateinit var iconImageView: ImageView
    protected lateinit var tickImageView: ImageView
    protected lateinit var labelTextView: TextView

    var isChecked: Boolean = false

    fun setIconProperties(bitmap: Bitmap, labelText: String, padding: Int) {
        this.iconImageView.setImageBitmap(bitmap)
        setIconLabelAndPadding(labelText, padding)
    }

    fun setIconProperties(drawable: Drawable, labelText: String, padding: Int) {
        this.iconImageView.setImageDrawable(drawable)
        setIconLabelAndPadding(labelText, padding)
    }

    private fun setIconLabelAndPadding(labelText: String, padding: Int) {
        this.labelTextView.text = labelText

        //Set the icon size by the padding from its dimensions (Higher padding = smaller icon)
        val scale = resources.displayMetrics.density
        val actualPadding = (scale * padding + 0.5).toInt()
        this.findViewById<ConstraintLayout>(R.id.cnlIngredientSubcatSearchIconComponent)
            .setPadding(actualPadding, actualPadding, actualPadding, actualPadding)
    }
}