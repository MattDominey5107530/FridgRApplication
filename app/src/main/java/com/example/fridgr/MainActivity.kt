package com.example.fridgr

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Preload the fridge tab into the view and set the navigation bar to show that you're on that page
        toolbar.title = "Fridge"
        val fridgeFragment = FridgeFragment.newInstance()
        openFragment(fridgeFragment)
        bottomNavigation.selectedItemId = R.id.navigation_fridge

        /** TESTING API FUNCTIONS */
        var response: Any?
        //response = APIHandlerClass.getRecipeListBySearch("Curry", listOf("SHELLFISH"), "VEGETARIAN", listOf("INDIAN"), "MAIN_COURSE")
        //response = APIHandlerClass.getRecipeListByIngredients(listOf(TestIngredient("HEALTH_FOODS", 1F, 1, "", "Apple", "kg")))
        //response = APIHandlerClass.getRecipeInfo(1426917)
        //response = APIHandlerClass.getRecipeInstructions(1426917)
    }

    /**
     * Handles the changing between fragments from the BottomNavigationView
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_profile -> {
                toolbar.title = "Profile"
                val profileFragment = ProfileFragment.newInstance()
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favourites -> {
                toolbar.title = "Favourites"
                val favouritesFragment = FavouritesFragment.newInstance()
                openFragment(favouritesFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_fridge -> {
                toolbar.title = "Fridge"
                val fridgeFragment = FridgeFragment.newInstance()
                openFragment(fridgeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_recipesearch -> {
                toolbar.title = "Recipe Search"
                val recipeSearchFragment = RecipeSearchFragment.newInstance()
                openFragment(recipeSearchFragment)
                return@OnNavigationItemSelectedListener true
            }
            //TODO: Remove after creating and testing all GUI elements
            R.id.navigation_settings -> {
                toolbar.title = "Testing"
                val testFragment = LoginFragment.newInstance()
                openFragment(testFragment)
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

}


