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

    lateinit var recipeSearchEditText: EditText
    lateinit var filterButton: Button
    lateinit var sortButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_favourites, container, false)

        recipeSearchEditText = v.findViewById(R.id.edtFavouritesSearch)
        filterButton = v.findViewById(R.id.btnFilter)
        sortButton = v.findViewById(R.id.btnSort)

        recipeSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { onChangeSearchText() }

            //Redundant but necessary functions to satisfy abstractTextWatcher
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        filterButton.setOnClickListener { onClickFilter() }
        sortButton.setOnClickListener { onClickSort() }

        //TODO: populate the favouritesRecipeContainer with recipes from the local users favourites

        return v
    }

    fun onChangeSearchText() {
        //TODO: actually search through the list of recipes
    }

    fun onClickSort() {
        //TODO: add functionality of sorting the list
        // potentially a popup window with sorting criteria
        // potentially a small window underneath button with different sorting methods, e.g.
        // A-Z name, ingredient number?,
        // Also, do we really need this?
    }

    fun onClickFilter() {
        //TODO: add functionality of filtering the list
        // potentially a popup window with filtering criteria
    }
}