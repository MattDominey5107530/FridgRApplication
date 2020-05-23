package com.example.fridgr.ingredient_search_component

import android.content.Context
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.*
import com.example.fridgr.R
import com.example.fridgr.local_storage.UserPreferences
import com.example.fridgr.local_storage.getUserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IngredientSearchComponent(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var ingredientSearchEditText: EditText
    private var ingredientListButton: Button

    private var subcatIconRecyclerView: RecyclerView
    private var subcatIconLayoutManager: LinearLayoutManager
    private var subcatIconAdapter: SubcatIconAdapter

    private var ingredientIconRecyclerView: RecyclerView
    private var ingredientIconLayoutManager: GridLayoutManager
    private var ingredientIconAdapter: IngredientIconAdapter

    private var currentSubcatIndex: Int = 0
    private var ingredients = listOf<Ingredient>()
    private val userPreferences: UserPreferences?

    var checkedIngredients = arrayListOf<Ingredient>()

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.ingredient_subcat_search_component, this, true)

        ingredientSearchEditText = findViewById(R.id.edtIngredientSearch)
        ingredientListButton = findViewById(R.id.btnIngredientList)

        subcatIconLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        subcatIconAdapter = SubcatIconAdapter(arrayListOf())
        subcatIconRecyclerView =
            findViewById<RecyclerView>(R.id.rcvSubcatIconContainer).apply {
                setHasFixedSize(true)
                layoutManager = subcatIconLayoutManager
                adapter = subcatIconAdapter
            }

        ingredientIconLayoutManager =
            GridLayoutManager(context, 3, LinearLayoutManager.HORIZONTAL, false)
        ingredientIconAdapter = IngredientIconAdapter(arrayListOf())
        ingredientIconRecyclerView =
            findViewById<RecyclerView>(R.id.rcvIngredientIconContainer).apply {
                setHasFixedSize(true)
                layoutManager = ingredientIconLayoutManager
                adapter = ingredientIconAdapter
            }

        ingredientSearchEditText.addTextChangedListener(object : TextWatcher {
            //Redundant but necessary functions to satisfy abstractTextWatcher
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onChangeSearchText(p0.toString())
            }
        })

        userPreferences = getUserPreferences(context)
    }

    inner class IngredientIconAdapter(var myDataset: List<Ingredient>) :
        RecyclerView.Adapter<IngredientIconAdapter.IngredientIconViewHolder>() {

        inner class IngredientIconViewHolder(val ingredientIcon: IngredientSearchIcon) :
            RecyclerView.ViewHolder(ingredientIcon)

        //Create the icon
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): IngredientIconViewHolder {

            return IngredientIconViewHolder(IngredientSearchIcon(context))
        }

        //Populate the ingredient icon
        override fun onBindViewHolder(holder: IngredientIconViewHolder, position: Int) {
            with(holder.ingredientIcon) {
                //Set the fields of the icon (text, icon etc.)
                setProperties(myDataset[position])

                //Make sure it's checked if it's currently in the check ingredients list
                if (myDataset[position] in checkedIngredients) {
                    setCheckedState(true)
                } else {
                    setCheckedState(false)
                }

                //onClick toggle's the ingredient's checked state and adds/removes it from the
                // checked ingredients list. Also update the number on the ingredient list button
                setOnClickListener {
                    onClick()
                    if (isChecked) {
                        checkedIngredients.add(myDataset[position])
                    } else {
                        checkedIngredients.remove(myDataset[position])
                    }
                    updateIngredientListButtonText()
                }
            }
        }

        override fun getItemCount() = myDataset.size
    }

    inner class SubcatIconAdapter(var myDataset: List<String>) :
        RecyclerView.Adapter<SubcatIconAdapter.SubcatIconViewHolder>() {

        inner class SubcatIconViewHolder(val subcatIconButton: ImageButton) :
            RecyclerView.ViewHolder(subcatIconButton)

        //Create the icon
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SubcatIconViewHolder {

            return SubcatIconViewHolder(ImageButton(context))
        }

        //Populate the icon
        override fun onBindViewHolder(holder: SubcatIconViewHolder, position: Int) {
            with(holder.subcatIconButton) {
                val backgroundId = when (myDataset[position]) {
                    "Relevant" -> R.drawable.relevant_button
                    "Baking" -> R.drawable.baking_button
                    "Health Foods" -> R.drawable.health_foods_button
                    "Spices and Seasonings" -> R.drawable.spices_button
                    "Pasta & Rice" -> R.drawable.rice_pasta_button
                    "Bakery/Bread" -> R.drawable.baking_button
                    "Refrigerated" -> R.drawable.refrigerated_button
                    "Canned and Jarred" -> R.drawable.canned_and_jarred_button
                    "Frozen" -> R.drawable.frozen_button
                    "Nut butters, Jams, and Honey" -> R.drawable.butter_jam_button
                    "Oil, Vinegar, Salad Dressing" -> R.drawable.oil_vinegar_button
                    "Condiments" -> R.drawable.condiments_button
                    "Savory Snacks" -> R.drawable.savory_snacks_button
                    "Milk, Eggs, Other Dairy" -> R.drawable.dairy_button
                    "Ethnic Foods" -> R.drawable.ethnic_food
                    "Tea and Coffee" -> R.drawable.tea_coffee_button
                    "Meat" -> R.drawable.meat_button
                    "Gourmet" -> R.drawable.gourmet_button
                    "Sweet Snacks" -> R.drawable.sweet_snacks_button
                    "Gluten Free" -> R.drawable.gluten_free_button
                    "Alcoholic Beverages" -> R.drawable.alcoholic_beverages_button
                    "Cereal" -> R.drawable.cereal_button
                    "Nuts" -> R.drawable.nuts_button
                    "Beverages" -> R.drawable.beverages_button
                    "Produce" -> R.drawable.produce_button
                    "Not in Grocery Store/Homemade" -> R.drawable.homemade_button
                    "Seafood" -> R.drawable.seafood_button
                    "Cheese" -> R.drawable.cheese_button
                    "Dried Fruits" -> R.drawable.dried_fruits_button
                    "Online" -> R.drawable.online_button
                    "Grilling Supplies" -> R.drawable.grilling_supplies_button
                    "Bread" -> R.drawable.bread__button
                    else -> R.drawable.plus_button
                }
                background = ContextCompat.getDrawable(context, backgroundId)
                val density = context.resources.displayMetrics.density
                layoutParams = LayoutParams((120 * density).toInt(), (145 * density).toInt())
                scaleType = ImageView.ScaleType.FIT_CENTER

                if (currentSubcatIndex == position) {
                    isPressed = true
                }

                setOnClickListener {
                    uncheckAllSubcatIcons()
                    it.isPressed = true
                    currentSubcatIndex = position
                    ingredientIconAdapter.myDataset =
                        if (currentSubcatIndex == 0) {
                            //If 'relevant', then just show all the ingredients returned by the search
                            ingredients
                        } else {
                            //Only show ingredients from that aisle
                            val aisle: Aisle =
                                getAisleFromAisleString(myDataset[position])
                            ingredients.filter { ingredient ->
                                ingredient.aisle.any { itAisle -> itAisle == aisle }
                            }
                        }
                    ingredientIconAdapter.notifyDataSetChanged()
                }
            }
        }

        // Return the size of your dataset
        override fun getItemCount() = myDataset.size
    }

    private fun setIngredients(ingredientList: List<Ingredient>) {
        currentSubcatIndex = 0
        with(ingredientIconAdapter) {
            ingredients = ingredientList
            myDataset = ingredients
            notifyDataSetChanged()
        }
    }

    private fun setSubcategories(subcatList: List<String>) {
        with(subcatIconAdapter) {
            val newSubcatList = subcatList.toMutableList()
            newSubcatList.add(0, "Relevant")
            myDataset = newSubcatList.toList()
            notifyDataSetChanged()
        }
    }

    //Update the icon check states according to a new checkedIngredientsList from the PopupWindow
    fun updateCheckedIngredients(newCheckedIngredients: ArrayList<Ingredient>) {
        checkedIngredients = newCheckedIngredients
        ingredientIconAdapter.notifyDataSetChanged()
        ingredientListButton.text = checkedIngredients.size.toString()
    }

    fun updateIngredientListButtonText() {
        ingredientListButton.text = checkedIngredients.size.toString()
    }

    private fun onChangeSearchText(text: String) {
        //TODO: Request autocompleted ingredients from the API to display in this control
        // asyncronously, maybe add buffering animation while it loads in
        // IDEA: save quota by waiting a short time before asking for autocomplete for the control

        val userIntolerances = userPreferences?.intolerances ?: emptyList()

        CoroutineScope(IO).launch {
            val ingredientList: List<Ingredient> =
                SpoonacularAPIHandler.getAutocompletedIngredientList(text, userIntolerances)

            //Add all the unique subcats
            val subcatStringList = ArrayList<String>()
            ingredientList.forEach { ingredient ->
                ingredient.aisle.forEach { aisle ->
                    val aisleString = getAisleStringFromAisle(aisle)
                    if (aisleString !in subcatStringList) subcatStringList.add(aisleString)
                }
            }

            withContext(Main) {
                setIngredients(ingredientList)
                setSubcategories(subcatStringList)
            }
        }


    }

    private fun addIngredientToCheckedList(ingredient: Ingredient) {
        checkedIngredients.add(ingredient)
    }

    private fun uncheckAllSubcatIcons() {
//        for (i in 1..subcatIconRecyclerView.childCount) {
//            val child = subcatIconRecyclerView.getChildAt(i - 1) as SubcatSearchIcon
//            child.setCheckedState(false)
//        }
        for (subcatButton in subcatIconRecyclerView.children) {
            subcatButton.isPressed = false
        }
    }
}