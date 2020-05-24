package com.example.fridgr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Ingredient
import api.IngredientSearchRecipe
import api.Recipe
import com.example.fridgr.local_storage.getFavouriteRecipes
import com.example.fridgr.recyclerViewAdapters.IngredientSearchRecipeListAdapter
import com.example.fridgr.recyclerViewAdapters.RecipeListAdapter

class RecipeListFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null,
            ingredientSearchRecipes: List<IngredientSearchRecipe>
        ): RecipeListFragment =
            RecipeListFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
                this.ingredientSearchRecipes = ingredientSearchRecipes
            }
    }

    private lateinit var recyclerViewRecipeList: RecyclerView
    private lateinit var recyclerViewRecipeListAdapter: IngredientSearchRecipeListAdapter
    private lateinit var recyclerViewRecipeListLayoutManager: RecyclerView.LayoutManager
    private lateinit var ingredientSearchRecipes: List<IngredientSearchRecipe>

    private val favouriteRecipes = ArrayList<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        //Get the favourite recipes so that the favourite button can be set
        val tempFavouriteRecipes = getFavouriteRecipes(v.context)
        if (tempFavouriteRecipes != null) {
            favouriteRecipes.addAll(tempFavouriteRecipes)
        }

        v.findViewById<ImageButton>(R.id.imbBack)
            .setOnClickListener {
                switchToFragment(
                    this,
                    myParentFragment!!
                ) //Will always have the parent of the ingredient search
            }

        if (ingredientSearchRecipes.isNotEmpty()) {
            recyclerViewRecipeList = v.findViewById<RecyclerView>(R.id.rcvRecipeList).apply {
                recyclerViewRecipeListAdapter =
                    IngredientSearchRecipeListAdapter(context, favouriteRecipes, this, ::showRecipeFragment, ingredientSearchRecipes)
                recyclerViewRecipeListLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                layoutManager = recyclerViewRecipeListLayoutManager
                adapter = recyclerViewRecipeListAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
            v.findViewById<TextView>(R.id.txvNoRecipesMatch).visibility = View.GONE
        } else {
            v.findViewById<TextView>(R.id.txvNoRecipesMatch).visibility = View.VISIBLE
        }


        return v
    }

    private fun showRecipeFragment(recipeId: Int) {
        val recipeFragment = RecipeFragment.newInstance(switchToFragment, this, recipeId)
        switchToFragment(this, recipeFragment)
    }
}