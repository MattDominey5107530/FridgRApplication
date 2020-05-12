package com.example.fridgr

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Preload the fridge tab into the view and set the navigation bar to show that you're on that page
        val fridgeFragment = FridgeFragment.newInstance()
        openFragment(fridgeFragment)
        bottomNavigation.selectedItemId = R.id.navigation_fridge
    }

    /**
     * Handles the changing between fragments from the BottomNavigationView
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_profile -> {
                val profileFragment = ProfileFragment.newInstance()
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favourites -> {
                val favouritesFragment = FavouritesFragment.newInstance()
                openFragment(favouritesFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_fridge -> {
                val fridgeFragment = FridgeFragment.newInstance()
                openFragment(fridgeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_recipesearch -> {
                val recipeSearchFragment = RecipeSearchFragment.newInstance()
                openFragment(recipeSearchFragment)
                return@OnNavigationItemSelectedListener true
            }
            //TODO: Remove after creating and testing all GUI elements
            R.id.navigation_settings -> {
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


