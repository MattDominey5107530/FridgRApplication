package com.example.fridgr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.*
import com.example.fridgr.local_storage.UserPreferences
import com.example.fridgr.local_storage.getFavouriteRecipes
import com.example.fridgr.local_storage.getUserCuisines
import com.example.fridgr.local_storage.getUserPreferences
import com.example.fridgr.recyclerViewAdapters.RecipeListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeSearchFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): RecipeSearchFragment =
            RecipeSearchFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private lateinit var recyclerViewRecipeList: RecyclerView
    private lateinit var recyclerViewRecipeListAdapter: RecipeListAdapter
    private lateinit var recyclerViewRecipeListLayoutManager: RecyclerView.LayoutManager
    private lateinit var recipeSearchAutocompleteAdapter: ArrayAdapter<String>

    private lateinit var noRecipesMatchTextView: TextView

    private lateinit var favouriteRecipes: List<Recipe>
    private var userPreferences: UserPreferences? = null
    private lateinit var userCuisines: List<Cuisine>
    private var filters: Filters? = null

    data class Filters(
        val mealType: MealType,
        val nutritionFilters: NutritionFilters
    )

    data class NutritionFilters(
        val caloryRange: IntRange?,
        val fatRange: IntRange?,
        val proteinRange: IntRange?,
        val carbRange: IntRange?
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_recipe_search, container, false)

        v.findViewById<AutoCompleteTextView>(R.id.edtRecipeSearch).apply {
            recipeSearchAutocompleteAdapter = ArrayAdapter<String>(
                context!!,
                android.R.layout.select_dialog_item,
                emptyList()
            )
            threshold = 3
            setAdapter(recipeSearchAutocompleteAdapter)

            //Update the autocomplete options to reflect new text
            addTextChangedListener(object : TextWatcher {
                //Redundant but necessary functions to satisfy abstractTextWatcher
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    onChangeSearchText(p0.toString())
                }
            })

            //Hides the keyboard and searches if the user presses 1 of the autocomplete
            // options
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, _, _ ->
                    searchForRecipes(this.text.toString())
                    hideKeyboard()
                }
            //Searches if the user presses search within the soft keyboard
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchForRecipes(v?.text.toString())
                        hideKeyboard()
                        return true
                    }
                    return false
                }
            })
        }

        v.findViewById<ImageButton>(R.id.imbFilter).setOnClickListener {
            //TODO: onClick filter
        }

        noRecipesMatchTextView = v.findViewById<TextView>(R.id.txvNoRecipesMatch).apply {
            visibility = View.GONE
        }

        //Get the favourite recipes so that the favourite button can be set
        favouriteRecipes = getFavouriteRecipes(v.context) ?: emptyList()
        userPreferences = getUserPreferences(v.context)
        userCuisines = getUserCuisines(v.context) ?: emptyList()

        recyclerViewRecipeList = v.findViewById<RecyclerView>(R.id.rcvRecipeList).apply {
            recyclerViewRecipeListAdapter =
                RecipeListAdapter(context, favouriteRecipes, this, emptyList())
            recyclerViewRecipeListLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            layoutManager = recyclerViewRecipeListLayoutManager
            adapter = recyclerViewRecipeListAdapter
        }

        return v
    }

    private fun onChangeSearchText(text: String) {
        CoroutineScope(IO).launch {
            val autocompletedRecipes = SpoonacularAPIHandler.getAutocompletedRecipeList(text)

            withContext(Main) {
                if (autocompletedRecipes.isNotEmpty()) {
                    with(recipeSearchAutocompleteAdapter) {
                        clear()
                        addAll(autocompletedRecipes.map { it.title })
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun searchForRecipes(text: String) {
        if (text != "") {
            CoroutineScope(IO).launch {
                val recipesFromSearch = SpoonacularAPIHandler.getRecipeListBySearch(
                    text,
                    userPreferences?.intolerances ?: emptyList(),
                    userPreferences?.diet,
                    userCuisines,
                    filters?.mealType,
                    filters?.nutritionFilters
                )

                withContext(Main) {
                    if (recipesFromSearch.isNotEmpty()) {
                        with(recyclerViewRecipeListAdapter) {
                            myDataset = recipesFromSearch
                            notifyDataSetChanged()
                        }
                        noRecipesMatchTextView.visibility = View.GONE
                    } else {
                        noRecipesMatchTextView.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            Toast.makeText(context, "You must have some search text.", Toast.LENGTH_SHORT).show()
        }
    }
}