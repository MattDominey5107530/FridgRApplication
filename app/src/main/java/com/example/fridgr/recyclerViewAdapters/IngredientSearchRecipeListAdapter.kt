package com.example.fridgr.recyclerViewAdapters

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import api.IngredientSearchRecipe
import api.Recipe
import com.example.fridgr.R

class IngredientSearchRecipeListAdapter(
    private val context: Context,
    private val favouriteRecipes: List<Recipe>?,
    private val parentView: RecyclerView,
    private val showRecipeFragment: (Int) -> Unit,
    var myDataset: List<IngredientSearchRecipe>
) : RecyclerView.Adapter<IngredientSearchRecipeListAdapter.IngredientSearchRecipeViewHolder>() {

    inner class IngredientSearchRecipeViewHolder(val recipeComponent: RecipeComponent) :
        RecyclerView.ViewHolder(recipeComponent)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientSearchRecipeViewHolder {
        return IngredientSearchRecipeViewHolder(
            RecipeComponent(
                context,
                parentView
            )
        )
    }

    override fun onBindViewHolder(holder: IngredientSearchRecipeViewHolder, position: Int) {
        with(holder.recipeComponent) {
            //Set the description of the recipe
            val recipeInfo =
                when {
                    myDataset[position].missedIngredients.isNotEmpty() -> {
                        myDataset[position].missedIngredients.joinToString("\n") { "+ ${it.name}" }
                    }
                    myDataset[position].unusedIngredients.isNotEmpty() -> {
                        recipeInfoTextView.setTextColor(
                            ContextCompat.getColor(context, R.color.closing)
                        )
                        myDataset[position].unusedIngredients.joinToString("\n") { "- ${it.name}" }
                    }
                    else -> {
                        recipeInfoTextView.setTextColor(
                            ContextCompat.getColor(context, R.color.iconDarkColor)
                        )
                       "No additional ingredients needed"
                    }
                }

            val isFavouriteRecipe =
                favouriteRecipes?.any { it.id == myDataset[position].recipe.id } ?: false

            //Set the properties of the component
            setRecipe(
                myDataset[position].recipe,
                myDataset[position].recipe.name,
                recipeInfo,
                isFavouriteRecipe,
                myDataset[position].recipe.imageString
            )

            clickableAreaFrameLayout.setOnClickListener {
                showRecipeFragment.invoke(myDataset[position].recipe.id)
            }
        }
    }

    override fun getItemCount(): Int = myDataset.size
}