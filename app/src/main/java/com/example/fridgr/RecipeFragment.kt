package com.example.fridgr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class RecipeFragment: Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null
    private var recipeId: Int = -1

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null,
            recipeId: Int
        ): RecipeFragment =
            RecipeFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
                this.recipeId = recipeId
            }
    }

    /**
     * Fragment instantiation
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_login, container, false) //TODO: fragment layout

        //TODO: fetch using recipeId

        return v
    }

}