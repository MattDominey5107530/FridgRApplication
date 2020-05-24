package com.example.fridgr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Recipe
import com.example.fridgr.local_storage.getFavouriteRecipes
import com.example.fridgr.recyclerViewAdapters.RecipeListAdapter

class FavouritesFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): FavouritesFragment =
            FavouritesFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private var isActive: Boolean = false

    private lateinit var recyclerViewFavouriteRecipeList: RecyclerView
    private lateinit var recyclerViewFavouriteRecipeListAdapter: RecipeListAdapter
    private lateinit var recyclerViewFavouriteRecipeListLayoutManager: RecyclerView.LayoutManager
    private lateinit var noFavouriteRecipesTextView: TextView

    private var fullFavouriteRecipes: List<Recipe>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_favourites, container, false)

        val favouriteSearchEditText = v.findViewById<EditText>(R.id.edtFavouritesSearch)

        favouriteSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                onChangeSearchText(favouriteSearchEditText.text.toString())
            }

            //Redundant but necessary functions to satisfy abstractTextWatcher
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        noFavouriteRecipesTextView = v.findViewById(R.id.txvNoFavouriteRecipes)

        fullFavouriteRecipes = getFavouriteRecipes(context!!)
        if (fullFavouriteRecipes != null) {
            isActive = true
            recyclerViewFavouriteRecipeList =
                v.findViewById<RecyclerView>(R.id.rcvFavouriteRecipeList).apply {
                    recyclerViewFavouriteRecipeListAdapter =
                        RecipeListAdapter(context, fullFavouriteRecipes, this, fullFavouriteRecipes!!)
                    recyclerViewFavouriteRecipeListLayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                    layoutManager = recyclerViewFavouriteRecipeListLayoutManager
                    adapter = recyclerViewFavouriteRecipeListAdapter
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            noFavouriteRecipesTextView.visibility = View.GONE
        } else {
            noFavouriteRecipesTextView.visibility = View.VISIBLE
        }

        return v
    }

    /**
     * Function to search through and display the user's favourite recipes according to the
     *  search text.
     */
    fun onChangeSearchText(searchText: String) {
        if (isActive && fullFavouriteRecipes != null) {
            val filteredFavouriteRecipe: List<Recipe>? =
                fullFavouriteRecipes?.filter { it.name.contains(searchText, true) } ?: emptyList()

            if (filteredFavouriteRecipe != null && filteredFavouriteRecipe.isNotEmpty()) {
                with (recyclerViewFavouriteRecipeListAdapter) {
                    myDataset = filteredFavouriteRecipe
                    notifyDataSetChanged()
                }
                noFavouriteRecipesTextView.visibility = View.VISIBLE
            } else {
                noFavouriteRecipesTextView.visibility = View.GONE
            }
        }
    }
}