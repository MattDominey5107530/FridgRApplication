package com.example.fridgr.recyclerViewAdapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api.Recipe

class RecipeListAdapter(private val context: Context,
                        private val favouriteRecipes: List<Recipe>?,
                        private val parentView: RecyclerView,
                        private val showRecipeFragment: (Int) -> Unit,
                        var myDataset: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(val recipeComponent: RecipeComponent) :
        RecyclerView.ViewHolder(recipeComponent)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            RecipeComponent(
                context,
                parentView
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        with (holder.recipeComponent) {
            //Set the description of the recipe
            val recipeInfoList = ArrayList<String>()
            for (nutrition in myDataset[position].nutritionList) {
                recipeInfoList.add(nutrition.toString())
            }
            val recipeInfo = recipeInfoList.joinToString("\n")

            println(recipeInfo)

            val isFavouriteRecipe =
                favouriteRecipes?.any { it.id == myDataset[position].id } ?: false

            //Set the properties of the component
            setRecipe(
                myDataset[position],
                myDataset[position].name,
                recipeInfo,
                isFavouriteRecipe,
                myDataset[position].imageString
            )

            clickableAreaFrameLayout.setOnClickListener {
                showRecipeFragment.invoke(myDataset[position].id)
            }
        }
    }

    override fun getItemCount(): Int = myDataset.size
}