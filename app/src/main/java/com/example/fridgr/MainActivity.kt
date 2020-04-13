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
    }

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
            R.id.navigation_settings -> {
                toolbar.title = "Settings"
                val settingsFragment = SettingsFragment.newInstance()
                openFragment(settingsFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}


