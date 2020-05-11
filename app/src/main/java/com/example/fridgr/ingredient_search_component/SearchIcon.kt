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

    /**
     * package com.example.fridgr.ingredient_search_component

    import android.content.Context
    import android.graphics.Color
    import android.util.AttributeSet
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.constraintlayout.widget.ConstraintLayout
    import com.example.fridgr.R

    const val SUBCATEGORY_PADDING = 2
    const val INGREDIENT_PADDING = 5

    class IngredientSearchIconComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
    ) : ConstraintLayout(context, attrs, defStyle) {

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

    //Make subcategory icons big and ingredient icons smaller (SUBCATEGORY_PADDING & INGREDIENT_PADDING)
    val scale = resources.displayMetrics.density
    val constraintLayout: ConstraintLayout = this.findViewById(R.id.cnlIngredientSubcatSearchIconComponent)
    if (isSubcat) {
    val padding = (scale * SUBCATEGORY_PADDING + 0.5).toInt()
    constraintLayout.setPadding(padding, padding, padding, padding)
    } else {
    val padding = (scale * INGREDIENT_PADDING + 0.5).toInt()
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
    //click events to the overall controller (IngredientSearchComponent)
    Log.v("X", "toggling checked state")
    if (!isSubcat) {
    setCheckedState(!this.isChecked)
    }
    }
    }
     */

}