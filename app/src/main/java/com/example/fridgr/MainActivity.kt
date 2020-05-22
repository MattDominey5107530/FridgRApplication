package com.example.fridgr

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import api.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Preload the fridge tab into the view and set the navigation bar to show that you're on that page
        val fridgeFragment = FridgeFragment.newInstance(::switchToFragment)
        openFragment(fridgeFragment)
        bottomNavigation.selectedItemId = R.id.navigation_fridge

        /** TESTING API FUNCTIONS */
        var response: Any?
        //response = SpoonacularAPIHandler.getRecipeListBySearch("Curry", listOf("SHELLFISH", "GLUTEN"), "VEGETARIAN", listOf("INDIAN"), "MAIN_COURSE")
        //response = SpoonacularAPIHandler.getRecipeListByIngredients(listOf(TestIngredient("HEALTH_FOODS", 1F, 1, "", "Apple", "kg")))
        //response = SpoonacularAPIHandler.getRecipeInfo(1426917)
        //response = SpoonacularAPIHandler.getRecipeInstructions(1426917)


//        CoroutineScope(IO).launch {
//            val recipeList = SpoonacularAPIHandler.getRecipeListBySearch(
//                "Curry",
//                listOf(Intolerance.SHELLFISH, Intolerance.GLUTEN),
//                Diet.VEGETARIAN,
//                listOf(Cuisine.INDIAN),
//                MealType.MAIN_COURSE
//            )
//
//            withContext(Main) {
//                for (recipe in recipeList) {
//                    with (recipe) {
//                        Log.v("Recipe", "Id: $id")
//                        Log.v("Recipe","Name: $name")
//                        Log.v("Recipe","NutritionList: ${nutritionList.joinToString(", ") { it.toString() }}")
//                        Log.v("Recipe","ImageURL: $imageString")
//                        Log.v("Recipe","=-=--=-=-=-=-=-=-=-=-=-===-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-==-=-==--=")
//                    }
//                }
//            }
//        }

//        CoroutineScope(IO).launch {
//            val ingredientSearchRecipeList = SpoonacularAPIHandler.getRecipeListByIngredients(
//                listOf(
//                    Ingredient(
//                        1,
//                        "Apple",
//                        listOf(Aisle.HEALTH_FOODS),
//                        "XXX.jpg"
//                    )
//                )
//            )
//
//            withContext(Main) {
//                for (ingredientSearchRecipe in ingredientSearchRecipeList) {
//                    with(ingredientSearchRecipe) {
//                        Log.v("IngredientSearchRecipe", "Id: ${recipe.id}")
//                        Log.v("IngredientSearchRecipe", "Name: ${recipe.name}")
//                        Log.v(
//                            "IngredientSearchRecipe",
//                            "NutritionList: ${recipe.nutritionList.joinToString(", ") { it.toString() }}"
//                        )
//                        Log.v("IngredientSearchRecipe", "ImageURL: ${recipe.imageString}")
//                        Log.v("IngredientSearchRecipe", "Missed ingredients:")
//                        Log.v("IngredientSearchRecipe", "    ${missedIngredients.joinToString("\n\t") { "${it.name}: ${it.aisle}" }}")
//                        Log.v("IngredientSearchRecipe", "Used ingredients:")
//                        Log.v("IngredientSearchRecipe", "    ${usedIngredients.joinToString("\n\t") { "${it.name}: ${it.aisle}" }}")
//                        Log.v("IngredientSearchRecipe", "Unused ingredients:")
//                        Log.v("IngredientSearchRecipe", "    ${unusedIngredients.joinToString("\n\t") { "${it.name}: ${it.aisle}" }}")
//                        Log.v(
//                            "IngredientSearchRecipe",
//                            "=-=--=-=-=-=-=-=-=-=-=-===-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-==-=-==--="
//                        )
//                    }
//                }
//            }
//        }


        //response = SpoonacularAPIHandler.getRecipeInfo(1426917)
        //response = SpoonacularAPIHandler.getRecipeInstructions(1426917)
        //response = SpoonacularAPIHandler.getAutocompletedIngredientList("App", listOf(Intolerance.GLUTEN, Intolerance.EGG))
    }

    /**
     * Handles the changing between fragments from the BottomNavigationView
     */
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    val profileFragment = ProfileFragment.newInstance(::switchToFragment)
                    openFragment(profileFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favourites -> {
                    val favouritesFragment = FavouritesFragment.newInstance(::switchToFragment)
                    openFragment(favouritesFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_fridge -> {
                    val fridgeFragment = FridgeFragment.newInstance(::switchToFragment)
                    openFragment(fridgeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_recipesearch -> {
                    val recipeSearchFragment = RecipeSearchFragment.newInstance(::switchToFragment)
                    openFragment(recipeSearchFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    /**
     * Helper function for mOnNavigationItemSelectedListener to change the fragment
     */
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /**
     * Helper function which switches to a new fragment without destroying the old one
     */
    private fun switchToFragment(oldFragment: Fragment, newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(oldFragment)

        if (supportFragmentManager.findFragmentById(newFragment.id) != null) {
            transaction.show(newFragment)
        } else {
            transaction.add(R.id.mainContainer, newFragment)
            transaction.show(newFragment)
        }
        transaction.commit()
    }

    /**
     * Disables the back button
     */
    override fun onBackPressed() {
        //super.onBackPressed()
    }

}


