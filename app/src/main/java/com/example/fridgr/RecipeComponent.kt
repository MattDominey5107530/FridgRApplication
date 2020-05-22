package com.example.fridgr

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("ViewConstructor")
class RecipeComponent @JvmOverloads constructor(
    context: Context,
    parentView: RecyclerView,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var recipeImageView: ImageView
    private var favouriteButton: ImageView
    private var recipeTextView: TextView
    private var recipeInfoTextView: TextView

    private var isRecipeFavourite: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.recipe_component, this, true).apply {
            layoutParams = parentView.layoutParams
        }

        //Get reference to drawn components
        recipeImageView = findViewById(R.id.imgRecipePicture)
        favouriteButton = findViewById(R.id.imgFavourite)
        recipeTextView = findViewById(R.id.txvRecipeName)
        recipeInfoTextView = findViewById(R.id.txvRecipeInfo)

        favouriteButton.setOnClickListener { onClickFavourite() }

    }

    private fun setImageBitmap(imageString: String) {
        //recipeImageView.setImageBitmap(bitmap)
        //TODO: set image with string using Picasso?
    }

    private fun setFavouriteState(isFavourite: Boolean) {
        isRecipeFavourite = isFavourite
        favouriteButton.apply {
            if (isFavourite) {
                setImageResource(R.drawable.ic_favourite)
                setColorFilter(ContextCompat.getColor(context, R.color.design_default_color_error))
            } else {
                setImageResource(R.drawable.ic_favourite)
                colorFilter = null
            }
        }
    }

    private fun setRecipeName(recipeName: String) {
        recipeTextView.text = recipeName
    }

    private fun setRecipeInfo(recipeInfo: String) {
        recipeInfoTextView.text = recipeInfo
    }

    fun setRecipe(
        recipeName: String,
        recipeInfo: String,
        isFavourite: Boolean,
        recipeImageString: String
    ) {
        setRecipeName(recipeName)
        setRecipeInfo(recipeInfo)
        setFavouriteState(isFavourite)
        setImageBitmap(recipeImageString)
    }

    /**
     * Handles the visual & functionality of 'favouriting' a recipe
     */
    private fun onClickFavourite() {
        isRecipeFavourite = !isRecipeFavourite
        setFavouriteState(isRecipeFavourite)
        //TODO: Add functionality to actually ADD the recipe to your favourites
    }


}