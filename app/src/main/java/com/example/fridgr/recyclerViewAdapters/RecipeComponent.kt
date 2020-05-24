package com.example.fridgr.recyclerViewAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import api.Recipe
import com.example.fridgr.R
import com.example.fridgr.local_storage.addRecipeToFavourites
import com.example.fridgr.local_storage.removeRecipeFromFavourites
import com.squareup.picasso.Picasso

@SuppressLint("ViewConstructor")
class RecipeComponent @JvmOverloads constructor(
    context: Context,
    parentView: RecyclerView,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val recipeImageView: ImageView
    private val favouriteButton: ImageView
    private val recipeTextView: TextView
    private val recipeInfoTextView: TextView

    val clickableAreaFrameLayout: FrameLayout

    private var isRecipeFavourite: Boolean = false

    private lateinit var recipe: Recipe

    init {
        LayoutInflater.from(context).inflate(R.layout.recipe_component, this, true).apply {
            layoutParams = parentView.layoutParams
        }

        //Get reference to drawn components
        recipeImageView = findViewById(R.id.imgRecipePicture)
        favouriteButton = findViewById(R.id.imgFavourite)
        recipeTextView = findViewById(R.id.txvRecipeName)
        recipeInfoTextView = findViewById(R.id.txvRecipeInfo)
        clickableAreaFrameLayout = findViewById(R.id.fmlClickableArea)

        favouriteButton.setOnClickListener { onClickFavourite() }

    }

    private fun setImageBitmap(imageString: String) {
        Picasso.get()
            .load(imageString)
            .resize(
                recipeImageView.layoutParams.width,
                recipeImageView.layoutParams.height)
            .centerCrop()
            .into(recipeImageView)
    }

    private fun setFavouriteState(isFavourite: Boolean) {
        isRecipeFavourite = isFavourite
        favouriteButton.apply {
            if (isFavourite) {
                setImageResource(R.drawable.ic_favourite)
                setColorFilter(ContextCompat.getColor(context,
                    R.color.design_default_color_error
                ))
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
        recipe: Recipe,
        recipeName: String,
        recipeInfo: String,
        isFavourite: Boolean,
        recipeImageString: String
    ) {
        this.recipe = recipe
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
        if (isRecipeFavourite) {
            addRecipeToFavourites(context, recipe)
        } else {
            removeRecipeFromFavourites(context, recipe)
        }
    }


}