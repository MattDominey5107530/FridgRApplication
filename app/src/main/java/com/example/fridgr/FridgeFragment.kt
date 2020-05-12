package com.example.fridgr

import android.annotation.TargetApi
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Ingredient
import com.example.fridgr.ingredient_search_component.IngredientSearchComponent
import com.example.fridgr.popups.AbstractPopup
import com.example.fridgr.popups.IngredientListPopup

class FridgeFragment : Fragment() {

    private lateinit var ingredientSearchComponent: IngredientSearchComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_fridge, container, false)

        //Creates the subcat search component and adds it to the specified frame layout
        ingredientSearchComponent =
            IngredientSearchComponent(v.context)
        v.findViewById<FrameLayout>(R.id.frlIngredientSubcatSearcher)
            .addView(ingredientSearchComponent)

        //onClick listeners
        v.findViewById<Button>(R.id.btnSearch).setOnClickListener { onClickSearch() }
        v.findViewById<Button>(R.id.btnIngredientList)
            .setOnClickListener { onClickIngredientList() }

        v.findViewById<EditText>(R.id.edtIngredientSearch)
            .setOnEditorActionListener { _, id: Int, _ ->
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }

        return v
    }

    private fun onClickSearch() {
        //TODO: getIngredientList from component; search for recipes through API and switch to recipesearch fragment (fragment to display the results of your search)
    }

    private fun onClickIngredientList() {
        //TODO: if theres no ingredients checked then dont show popupwindow!

        showIngredientListPopup()
    }

    companion object {
        fun newInstance(): FridgeFragment = FridgeFragment()
    }

    private fun onDismissIngredientListPopup(abstractPopup: AbstractPopup) {
        val ingredientListPopup = abstractPopup as IngredientListPopup
        val newCheckedIngredients = ingredientListPopup.newCheckedIngredients
        ingredientSearchComponent.updateCheckedIngredients(newCheckedIngredients)
    }

    private fun showIngredientListPopup() {
        IngredientListPopup(
            context!!,
            ::onDismissIngredientListPopup,
            ingredientSearchComponent.checkedIngredients
        )
            .showAtLocation(
                view!!,
                Gravity.CENTER,
                0,
                0
            )
    }
}