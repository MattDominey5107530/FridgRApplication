package com.example.fridgr

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class RecipeComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var recipeImageView: ImageView
    private var favouriteButton: ImageButton
    private var recipeTextView: TextView
    private var recipeInfoTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.recipe_component, this, true)

        //Get reference to drawn components
        recipeImageView = findViewById(R.id.imgRecipePicture)
        favouriteButton = findViewById(R.id.imgFavourite)
        recipeTextView = findViewById(R.id.txvRecipeName)
        recipeInfoTextView = findViewById(R.id.txvRecipeInfo)
    }

    fun setImageBitmap(bitmap: Bitmap) { recipeImageView.setImageBitmap(bitmap) }

    fun setFavouriteState(isFavourite: Boolean) {
        //TODO: set the image of the favourite button according to whether it should be 'checked' or not
    }

    fun setRecipeName(recipeName: String) { recipeTextView.text = recipeName }

    fun setRecipeInfo(recipeInfo: String) { recipeInfoTextView.text = recipeInfo }

    fun setRecipe(recipeName: String, recipeInfo: String, isFavourite: Boolean, recipeBitmap: Bitmap) {
        setRecipeName(recipeName)
        setRecipeInfo(recipeInfo)
        setFavouriteState(isFavourite)
        setImageBitmap(recipeBitmap)
    }

}