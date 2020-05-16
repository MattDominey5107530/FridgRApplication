package com.example.fridgr

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import api.Nutrition
import api.Recipe
import com.example.fridgr.ingredient_search_component.IngredientSearchComponent
import com.example.fridgr.popups.AbstractPopup
import com.example.fridgr.popups.IngredientListPopup

class FridgeFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(switchToFragment: (Fragment, Fragment) -> Unit,
                        parentFragment: Fragment? = null): FridgeFragment =
            FridgeFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }


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
        //TODO: make this method get the recipes from the API instead of hardcoded ones here
        val recipesFromAPI = listOf(
            Recipe(1, "Lasagne", listOf(Nutrition("Fat", 32.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.alcoholic_beverages)!!.toBitmap(100, 100)),
            Recipe(1, "Chicken pie", listOf(Nutrition("Fat", 45.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.bakery)!!.toBitmap(100, 100)),
            Recipe(1, "Lasagne", listOf(Nutrition("Fat", 32.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.alcoholic_beverages)!!.toBitmap(100, 100)),
            Recipe(1, "Chicken pie", listOf(Nutrition("Fat", 45.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.bakery)!!.toBitmap(100, 100)),
            Recipe(1, "Lasagne", listOf(Nutrition("Fat", 32.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.alcoholic_beverages)!!.toBitmap(100, 100)),
            Recipe(1, "Chicken pie", listOf(Nutrition("Fat", 45.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.bakery)!!.toBitmap(100, 100)),
            Recipe(1, "Lasagne", listOf(Nutrition("Fat", 32.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.alcoholic_beverages)!!.toBitmap(100, 100)),
            Recipe(1, "Chicken pie", listOf(Nutrition("Fat", 45.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.bakery)!!.toBitmap(100, 100)),
            Recipe(1, "Lasagne", listOf(Nutrition("Fat", 32.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.alcoholic_beverages)!!.toBitmap(100, 100)),
            Recipe(1, "Chicken pie", listOf(Nutrition("Fat", 45.0, "g")), ContextCompat.getDrawable(context!!, R.drawable.bakery)!!.toBitmap(100, 100))

        )

        val recipeSearchFrag = RecipeFragment.newInstance(switchToFragment, this, recipesFromAPI)
        switchToFragment.invoke(this, recipeSearchFrag)
    }

    private fun onClickIngredientList() {
        if (ingredientSearchComponent.checkedIngredients.size > 0) {
            showIngredientListPopup()
        } else {
            Toast.makeText(context, "Pick some ingredients first!", Toast.LENGTH_SHORT).show()
        }

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