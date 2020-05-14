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
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Aisle
import api.Ingredient
import api.getAisleFromAisleString
import com.example.fridgr.R

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
    var checkedIngredients = arrayListOf<Ingredient>()

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.ingredient_subcat_search_component, this, true)

        ingredientSearchEditText = findViewById(R.id.edtIngredientSearch)
        ingredientListButton = findViewById(R.id.btnIngredientList)

        subcatIconLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        subcatIconAdapter = SubcatIconAdapter(arrayListOf())
        subcatIconRecyclerView = findViewById<RecyclerView>(R.id.rcvSubcatIconContainer).apply {
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

    data class Subcat(val subcatLabel: String, val subcatImageResource: Int)

    inner class SubcatIconAdapter(var myDataset: List<Subcat>) :
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
                val backgroundId = when (myDataset[position].subcatLabel) {
                    "Relevant" -> R.drawable.relevant_button
                    Aisle.BAKING.name -> R.drawable.baking_button
                    Aisle.HEALTH_FOODS.name -> R.drawable.baking_button //TODO
                    Aisle.SPICES_AND_SEASONINGS.name -> R.drawable.spices_button
                    Aisle.PASTA_AND_RICE.name -> R.drawable.rice_pasta_button
                    Aisle.BAKERY.name -> R.drawable.baking_button
                    Aisle.REFRIGERATED.name -> R.drawable.baking_button //TODO
                    Aisle.CANNED_AND_JARRED.name -> R.drawable.baking_button//TODO
                    Aisle.FROZEN.name -> R.drawable.baking_button//TODO
                    Aisle.BUTTERS_JAMS.name -> R.drawable.butter_jam_button
                    Aisle.OIL_VINEGAR.name -> R.drawable.oil_vinegar_button
                    Aisle.CONDIMENTS.name -> R.drawable.baking_button//TODO
                    Aisle.SAVORY_SNACKS.name -> R.drawable.savory_snacks_button
                    Aisle.EGGS_DAIRY.name -> R.drawable.dairy_button
                    Aisle.ETHNIC_FOODS.name -> R.drawable.baking_button//TODO
                    Aisle.TEA_AND_COFFEE.name -> R.drawable.tea_coffee_button
                    Aisle.MEAT.name -> R.drawable.meat_button
                    Aisle.GOURMET.name -> R.drawable.baking_button//TODO
                    Aisle.SWEET_SNACKS.name -> R.drawable.sweet_snacks_button
                    Aisle.GLUTEN_FREE.name -> R.drawable.baking_button//TODO
                    Aisle.ALCOHOLIC_BEVERAGES.name -> R.drawable.alcoholic_beverages_button
                    Aisle.CEREAL.name -> R.drawable.cereal_button
                    Aisle.NUTS.name -> R.drawable.nuts_button
                    Aisle.BEVERAGES.name -> R.drawable.beverages_button
                    Aisle.PRODUCE.name -> R.drawable.baking_button//TODO
                    Aisle.HOMEMADE.name -> R.drawable.baking_button//TODO
                    Aisle.SEAFOOD.name -> R.drawable.seafood_button
                    Aisle.CHEESE.name -> R.drawable.cheese_button
                    Aisle.DRIED_FRUITS.name -> R.drawable.baking_button//TODO
                    Aisle.ONLINE.name -> R.drawable.baking_button//TODO
                    Aisle.GRILLING_SUPPLIES.name -> R.drawable.baking_button//TODO
                    Aisle.BREAD.name -> R.drawable.bread__button
                    else -> R.drawable.baking_button //TODO
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
                                getAisleFromAisleString(myDataset[position].subcatLabel)
                            ingredients.filter { ingredient ->
                                ingredient.aisle == aisle
                            }
                        }
                    ingredientIconAdapter.notifyDataSetChanged()
                }
            }
        }

        // Return the size of your dataset
        override fun getItemCount() = myDataset.size
    }

    fun setIngredients(ingredientList: List<Ingredient>) {
        currentSubcatIndex = 0
        with(ingredientIconAdapter) {
            ingredients = ingredientList
            myDataset = ingredients
            notifyDataSetChanged()
        }
    }

    private fun setSubcategories(subcatList: List<Subcat>) {
        with(subcatIconAdapter) {
            val newSubcatList = subcatList.toMutableList()
            newSubcatList.add(0, Subcat("Relevant", R.drawable.ic_black_heart))
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

        val tempIngredientList = listOf(
            Ingredient(
                1,
                "Apple",
                Aisle.PRODUCE,
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            ),
            Ingredient(
                2,
                "Banana",
                Aisle.MEAT,
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            )
        )

        val tempSubcatList = listOf(
            Subcat("Produce", R.drawable.ic_vegetables),
            Subcat("Meat", R.drawable.ic_meat)
        )

        setIngredients(tempIngredientList)
        setSubcategories(tempSubcatList)
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