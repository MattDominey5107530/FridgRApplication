package com.example.fridgr

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import api.IngredientSearchRecipe
import api.Nutrition
import api.Recipe
import api.SpoonacularAPIHandler
import com.example.fridgr.ingredient_search_component.IngredientSearchComponent
import com.example.fridgr.popups.AbstractPopup
import com.example.fridgr.popups.IngredientListPopup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FridgeFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): FridgeFragment =
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
        if (ingredientSearchComponent.checkedIngredients.isNotEmpty()) {
            CoroutineScope(IO).launch {
                //TODO: buffering animation?
                val recipesFromApi =
                    SpoonacularAPIHandler.getRecipeListByIngredients(ingredientSearchComponent.checkedIngredients)

                withContext(Main) {
                    if (recipesFromApi.isNotEmpty()) {
                        switchToRecipeList(recipesFromApi)
                    } else {
                        //Technically, should never happen if the Api is okay.
                        Toast.makeText(context, "No recipes can be found!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        } else {
            //Technically, should never happen if the Api is okay.
            Toast.makeText(context, "Pick some ingredients first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun switchToRecipeList(recipesFromApi: List<IngredientSearchRecipe>) {
        val recipeSearchFrag =
            RecipeListFragment.newInstance(switchToFragment, this, recipesFromApi)
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
                Gravity.CENTER_VERTICAL,
                0,
                -72
            )
    }
}