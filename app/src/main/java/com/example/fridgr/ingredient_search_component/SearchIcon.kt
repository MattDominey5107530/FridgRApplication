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
import com.example.fridgr.getCircularDrawable

open class SearchIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): ConstraintLayout(context, attrs, defStyle) {

    protected lateinit var iconImageView: ImageView
    protected lateinit var tickImageView: ImageView
    protected lateinit var labelTextView: TextView

    var isChecked: Boolean = false

    fun setIconProperties(bitmap: Bitmap, labelText: String) {
        this.iconImageView.setImageDrawable(bitmap.getCircularDrawable(resources))
        setIconLabel(labelText)
    }

    fun setIconProperties(drawable: Drawable, labelText: String) {
        this.iconImageView.setImageDrawable(drawable)
        setIconLabel(labelText)
    }

    private fun setIconLabel(labelText: String) {
        this.labelTextView.text = labelText

    }
}