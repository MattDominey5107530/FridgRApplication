package com.example.fridgr.recyclerViewAdapters

import android.app.ActionBar
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api.Nutrition
import api.Recipe
import com.example.fridgr.RecipeComponent

class RecipeListAdapter(private val context: Context,
                        private val favouriteRecipes: List<Recipe>?,
                        private val parentView: RecyclerView,
                        var myDataset: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(val recipeComponent: RecipeComponent) :
        RecyclerView.ViewHolder(recipeComponent)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(RecipeComponent(context, parentView))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        with (holder.recipeComponent) {
            //Set the description of the recipe
            val recipeInfoList = ArrayList<String>()
            for (nutrition in myDataset[position].nutritionList) {
                recipeInfoList.add(nutrition.toString())
            }
            val recipeInfo = recipeInfoList.joinToString("\n")

            Log.v("X", recipeInfo)
            Log.v("X", myDataset[position].nutritionList[0].toString())

            val isFavouriteRecipe =
                if (favouriteRecipes != null) myDataset[position] in favouriteRecipes else false

            //Set the properties of the component
            setRecipe(
                myDataset[position].name,
                recipeInfo,
                isFavouriteRecipe,
                myDataset[position].image
            )
        }
    }

    override fun getItemCount(): Int = myDataset.size
}