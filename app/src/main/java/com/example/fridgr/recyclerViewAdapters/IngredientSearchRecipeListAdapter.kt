package com.example.fridgr.recyclerViewAdapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api.IngredientSearchRecipe
import api.Recipe
import com.example.fridgr.RecipeComponent

class IngredientSearchRecipeListAdapter(
    private val context: Context,
    private val favouriteRecipes: List<Recipe>?,
    private val parentView: RecyclerView,
    var myDataset: List<IngredientSearchRecipe>
) : RecyclerView.Adapter<IngredientSearchRecipeListAdapter.IngredientSearchRecipeViewHolder>() {

    inner class IngredientSearchRecipeViewHolder(val recipeComponent: RecipeComponent) :
        RecyclerView.ViewHolder(recipeComponent)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientSearchRecipeViewHolder {
        return IngredientSearchRecipeViewHolder(RecipeComponent(context, parentView))
    }

    override fun onBindViewHolder(holder: IngredientSearchRecipeViewHolder, position: Int) {
        with(holder.recipeComponent) {
            //Set the description of the recipe
            val recipeInfo = "" //TODO: get a summary from the IngredientSearchRecipe

            val isFavouriteRecipe =
                favouriteRecipes?.any { it.id == myDataset[position].recipe.id } ?: false

            //Set the properties of the component
            setRecipe(
                myDataset[position].recipe.name,
                recipeInfo,
                isFavouriteRecipe,
                myDataset[position].recipe.imageString
            )

            //TODO: set the onclick listener to show the recipe fragment
        }
    }

    override fun getItemCount(): Int = myDataset.size
}