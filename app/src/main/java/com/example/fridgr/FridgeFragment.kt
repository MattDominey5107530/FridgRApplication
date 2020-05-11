package com.example.fridgr

import android.annotation.TargetApi
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.fridgr.ingredient_search_component.IngredientSearchComponent

class FridgeFragment : Fragment() {

    private lateinit var ingredientSubcatSearchComponent: IngredientSearchComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_fridge, container, false)

        //Creates the subcat search component and adds it to the specified frame layout
        ingredientSubcatSearchComponent =
            IngredientSearchComponent(v.context)
        v.findViewById<FrameLayout>(R.id.frlIngredientSubcatSearcher).addView(ingredientSubcatSearchComponent)
        initialiseIngredientSubcatSearchComponent()

        //onClick listeners
        v.findViewById<Button>(R.id.btnSearch).setOnClickListener { onClickSearch() }
        v.findViewById<Button>(R.id.btnIngredientList).setOnClickListener { onClickIngredientList() }

        return v
    }

    private fun initialiseIngredientSubcatSearchComponent() {
        //TODO: initialise the class with actual subcategories and ingredients
        ingredientSubcatSearchComponent.addSubcat("Fruit", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Banana", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addSubcat("Dairy", R.drawable.ic_dairy)

        ingredientSubcatSearchComponent.addIngredient("Dairy", "Milk", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cream", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Yoghurt", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cheese", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Milk", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cream", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Yoghurt", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cheese", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Milk", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cream", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Yoghurt", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cheese", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Milk", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cream", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Yoghurt", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cheese", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Milk", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cream", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Yoghurt", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cheese", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Milk", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cream", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Yoghurt", R.drawable.ic_dairy)
        ingredientSubcatSearchComponent.addIngredient("Dairy", "Cheese", R.drawable.ic_dairy)
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

    private fun showIngredientListPopup() {
        // Initialize a new layout inflater instance
        val inflater: LayoutInflater = LayoutInflater.from(this.context)

        // Inflate a custom view using layout inflater
        val v = inflater.inflate(R.layout.popupwindow_recipe_list, null)

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            v, // Custom view to show in popup window
            1000, // Width of popup window
            1500 // Window height
        )

        @TargetApi(21)
        popupWindow.elevation = 10.0F

        val closeButton: ImageButton = v.findViewById(R.id.btnCloseIngredientList)
        val ingredientListLinearLayout: LinearLayout = v.findViewById(R.id.lnlIngredientList)

        closeButton.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
        }

        for (checkedIngredient in ingredientSubcatSearchComponent.checkedIngredientList) {
            val checkedIngredientListComponent = inflater.inflate(R.layout.recipe_list_component, null)
            with (checkedIngredientListComponent) {
                findViewById<ImageButton>(R.id.btnDeleteIngredient).setOnClickListener { ingredientListLinearLayout.removeView(this) }
                findViewById<TextView>(R.id.txvIngredientName).text = checkedIngredient
            }
            ingredientListLinearLayout.addView(checkedIngredientListComponent)
        }


        //Handle closing of the PopupWindow if a click lands outside of the PopupWindow
        with (popupWindow) {
            setOnDismissListener {
                val newCheckedIngredients = ArrayList<String>()
                for (i in 0 until ingredientListLinearLayout.childCount) {
                    val x = ingredientListLinearLayout.getChildAt(i).findViewById<TextView>(R.id.txvIngredientName).text
                    newCheckedIngredients.add(x.toString())
                }
                ingredientSubcatSearchComponent.updateCheckedIngredients(newCheckedIngredients)
            }
            isOutsideTouchable = true
            isFocusable = true
        }

        //Show the popup window on app
        popupWindow.showAtLocation(
            this.view, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            -75 // Y offset
        )
    }
}