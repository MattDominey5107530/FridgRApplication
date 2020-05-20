package com.example.fridgr

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fridgr.local_storage.logoutUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import user_database.UserDatabaseHandler
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: temp
        logoutUser(applicationContext)

        //TODO: any database tests



        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Preload the fridge tab into the view and set the navigation bar to show that you're on that page
        val fridgeFragment = FridgeFragment.newInstance(::switchToFragment)
        openFragment(fridgeFragment)
        bottomNavigation.selectedItemId = R.id.navigation_fridge
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


