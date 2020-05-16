package com.example.fridgr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Nutrition
import api.Recipe
import com.example.fridgr.local_storage.getFavouriteRecipes
import com.example.fridgr.recyclerViewAdapters.RecipeListAdapter

class RecipeSearchFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(switchToFragment: (Fragment, Fragment) -> Unit,
                        parentFragment: Fragment? = null): RecipeSearchFragment =
            RecipeSearchFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private lateinit var recyclerViewRecipeList: RecyclerView
    private lateinit var recyclerViewRecipeListAdapter: RecipeListAdapter
    private lateinit var recyclerViewRecipeListLayoutManager: RecyclerView.LayoutManager

    private val favouriteRecipes = ArrayList<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_recipe_search, container, false)

        v.findViewById<EditText>(R.id.edtRecipeSearch).addTextChangedListener(object : TextWatcher {
            //Redundant but necessary functions to satisfy abstractTextWatcher
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO: onChangeSearchText(p0.toString())
            }
        })

        v.findViewById<ImageButton>(R.id.imbFilter).setOnClickListener {
            //TODO: onClick filter
        }

        //Get the favourite recipes so that the favourite button can be set
        val tempFavouriteRecipes = getFavouriteRecipes(v.context)
        if (tempFavouriteRecipes != null) {
            favouriteRecipes.addAll(tempFavouriteRecipes)
        }

        //TODO: Temp
        val recipeList = listOf(
            Recipe(1, "Lasagne", listOf(Nutrition("Fat", 32.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.alcoholic_beverages)!!.toBitmap(100, 100)),
            Recipe(1, "Chicken pie", listOf(Nutrition("Fat", 45.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.bakery)!!.toBitmap(100, 100))
        )


        recyclerViewRecipeList = v.findViewById<RecyclerView>(R.id.rcvRecipeList).apply {
            recyclerViewRecipeListAdapter = RecipeListAdapter(context, favouriteRecipes, this, recipeList)
            recyclerViewRecipeListLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            layoutManager = recyclerViewRecipeListLayoutManager
            adapter = recyclerViewRecipeListAdapter
        }

        return v
    }
}