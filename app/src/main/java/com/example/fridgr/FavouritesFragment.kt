package com.example.fridgr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
        fun newInstance(switchToFragment: (Fragment, Fragment) -> Unit,
                        parentFragment: Fragment? = null): FavouritesFragment =
            FavouritesFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private var isActive: Boolean = false

    private lateinit var recyclerViewFavouriteRecipeList: RecyclerView
    private lateinit var recyclerViewFavouriteRecipeListAdapter: RecyclerView.Adapter<*>
    private lateinit var recyclerViewFavouriteRecipeListLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_favourites, container, false)

        v.findViewById<ImageButton>(R.id.imbFilter)
            .setOnClickListener {
                //TODO: filter through the favourites
            }

        v.findViewById<EditText>(R.id.edtFavouritesSearch)
            .addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { onChangeSearchText() }

            //Redundant but necessary functions to satisfy abstractTextWatcher
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        val favouriteRecipes = getFavouriteRecipes(context!!)
        if (favouriteRecipes != null) {
            isActive = true
            recyclerViewFavouriteRecipeList = v.findViewById<RecyclerView>(R.id.rcvRecipeList).apply {
                recyclerViewFavouriteRecipeListAdapter =
                    RecipeListAdapter(context, favouriteRecipes, this, favouriteRecipes)
                recyclerViewFavouriteRecipeListLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                layoutManager = recyclerViewFavouriteRecipeListLayoutManager
                adapter = recyclerViewFavouriteRecipeListAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        } else {
            //TODO: show something saying that you have no favourites or you're not logged in
        }

        return v
    }

    fun onChangeSearchText() {
        if (isActive) {}
        //TODO: actually search through the list of recipes
    }

    fun onClickFilter() {
        //TODO: add functionality of filtering the list
        // potentially a popup window with filtering criteria
    }
}