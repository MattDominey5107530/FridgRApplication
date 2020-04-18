package com.example.fridgr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class FridgeFragment : Fragment() {

    private lateinit var ingredientSubcatSearchComponent: IngredientSubcatSearchComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_fridge, container, false)

        //Creates the subcat search component and adds it to the specified frame layout
        ingredientSubcatSearchComponent = IngredientSubcatSearchComponent(v.context)
        v.findViewById<FrameLayout>(R.id.frlIngredientSubcatSearcher).addView(ingredientSubcatSearchComponent)
        initialiseIngredientSubcatSearchComponent()
        //TODO: add listener to detect when an ingredient is clicked in above object^

        return v
    }

    private fun initialiseIngredientSubcatSearchComponent() {
        //TODO: initialise the class with actual subcategories and ingredients
        ingredientSubcatSearchComponent.addSubcat("Fruit", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
        ingredientSubcatSearchComponent.addIngredient("Fruit", "Apple", R.drawable.ic_fruit)
    }

    companion object {
        fun newInstance(): FridgeFragment = FridgeFragment()
    }
}
