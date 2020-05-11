package com.example.fridgr.ingredient_search_component

import android.content.Context
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Aisle
import api.Ingredient
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

    var checkedIngredients = arrayListOf<Ingredient>()

    init {
        LayoutInflater.from(context).inflate(R.layout.ingredient_subcat_search_component, this, true)

        ingredientSearchEditText = findViewById(R.id.edtIngredientSearch)
        ingredientListButton = findViewById(R.id.btnIngredientList)

        subcatIconLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        subcatIconAdapter = SubcatIconAdapter(arrayListOf())
        subcatIconRecyclerView = findViewById<RecyclerView>(R.id.rcvSubcatIconContainer).apply {
            setHasFixedSize(true)
            layoutManager = subcatIconLayoutManager
            adapter = subcatIconAdapter
        }

        ingredientIconLayoutManager = GridLayoutManager(context, 3, LinearLayoutManager.HORIZONTAL, false)
        ingredientIconAdapter = IngredientIconAdapter(arrayListOf())
        ingredientIconRecyclerView = findViewById<RecyclerView>(R.id.rcvIngredientIconContainer).apply {
            setHasFixedSize(true)
            layoutManager = ingredientIconLayoutManager
            adapter = ingredientIconAdapter
        }

        ingredientSearchEditText.addTextChangedListener(object : TextWatcher {
            //Redundant but necessary functions to satisfy abstractTextWatcher
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { onChangeSearchText(p0.toString()) }
        })
    }

    inner class IngredientIconAdapter(var myDataset: List<Ingredient>) :
        RecyclerView.Adapter<IngredientIconAdapter.IngredientIconViewHolder>() {

        inner class IngredientIconViewHolder(val ingredientIcon: IngredientSearchIcon) : RecyclerView.ViewHolder(ingredientIcon)

        //Create the icon
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): IngredientIconViewHolder {

            return IngredientIconViewHolder(IngredientSearchIcon(context))
        }

        //Populate the icon
        override fun onBindViewHolder(holder: IngredientIconViewHolder, position: Int) {
            with (holder.ingredientIcon) {
                setProperties(myDataset[position])
                setOnClickListener {
                    //Toggle the ingredient's checked state
                    setCheckedState(!isChecked)
                    //Add or remove it from the list depending on whether it is now checked
                    if (isChecked) {
                        checkedIngredients.add(myDataset[position])
                    } else {
                        checkedIngredients.remove(myDataset[position])
                    }
                }
            }
            //set checked state according to checked ingredient list
        }

        // Return the size of your dataset
        override fun getItemCount() = myDataset.size
    }

    data class Subcat(val subcatLabel: String, val subcatImageResource: Int)

    inner class SubcatIconAdapter(var myDataset: List<Subcat>) :
        RecyclerView.Adapter<SubcatIconAdapter.SubcatIconViewHolder>() {

        inner class SubcatIconViewHolder(val subcatIcon: SubcatSearchIcon) : RecyclerView.ViewHolder(subcatIcon)

        //Create the icon
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): SubcatIconViewHolder {

            return SubcatIconViewHolder(SubcatSearchIcon(context))
        }

        //Populate the icon
        override fun onBindViewHolder(holder: SubcatIconViewHolder, position: Int) {
            with (myDataset[position]) {
                holder.subcatIcon.setProperties(subcatLabel, subcatImageResource, ::uncheckAllSubcatIcons)
            }
        }

        // Return the size of your dataset
        override fun getItemCount() = myDataset.size
    }


    fun setIngredients(ingredientList: List<Ingredient>) {
        with (ingredientIconAdapter) {
            myDataset = ingredientList
            notifyDataSetChanged()
        }
    }

    private fun setSubcategories(subcatList: List<Subcat>) {
        with (subcatIconAdapter) {
            myDataset = subcatList
            notifyDataSetChanged()
        }
    }

    //Update the icon check states according to a new checkedIngredientsList from the PopupWindow
    fun updateCheckedIngredients(newCheckedIngredients: ArrayList<Ingredient>) {
        checkedIngredients = newCheckedIngredients
        ingredientIconAdapter.notifyDataSetChanged()
        ingredientListButton.text = checkedIngredients.size.toString()
    }


    private fun onChangeSearchText(text: String) {
        //TODO: display search icon in subcat and search for ingredients and set those ingredients in the ingredient gridview (setIngredientIcons())
        //TODO: also, if there is search text, then show search icon if not then don't (setSearchVisibility())
        //text is ingredientSearchEditText.text
        //Gets triggered after every change to the search text

        Log.v("X", "Type event")
        //TODO: TEMP Testing

        val tempIngredientList = listOf(
            Ingredient(1, "Apple", Aisle.PRODUCE, Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)),
            Ingredient(2, "Banana", Aisle.PRODUCE, Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
        )

        val tempSubcatList = listOf(
            Subcat("Produce", R.drawable.ic_vegetables)
        )

        setIngredients(tempIngredientList)
        setSubcategories(tempSubcatList)


    }

    private fun addIngredientToCheckedList(ingredient: Ingredient) {
        checkedIngredients.add(ingredient)
    }

    private fun uncheckAllSubcatIcons() {
        for (i in 1..subcatIconRecyclerView.childCount) {
            val child = subcatIconRecyclerView.getChildAt(i-1) as SubcatSearchIcon
            child.setCheckedState(false)
        }
    }
}