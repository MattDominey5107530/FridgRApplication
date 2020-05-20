package com.example.fridgr.profile_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.Diet
import api.Intolerance
import com.example.fridgr.ProfileFragment
import com.example.fridgr.R
import com.example.fridgr.local_storage.*

class ProfileSettingsFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): ProfileSettingsFragment =
            ProfileSettingsFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_profile_settings, container, false)

        //Set the onClicks
        v.findViewById<ImageButton>(R.id.imbBack).setOnClickListener {
            (myParentFragment as ProfileFragment).updateFields()
            switchToFragment(this, myParentFragment!!)
        }

        v.findViewById<Button>(R.id.btnDietaryRequirements).setOnClickListener {
            val dietsFragment = DietsFragment.newInstance(switchToFragment, this)
            switchToFragment(this, dietsFragment)
        }

        v.findViewById<Button>(R.id.btnFavouriteCuisines).setOnClickListener {
            val cuisinesFragment = CuisinesFragment.newInstance(switchToFragment, this)
            switchToFragment(this, cuisinesFragment)
        }

        v.findViewById<Button>(R.id.btnMembershipSettings).setOnClickListener {
            //TODO: Make membership settings fragment
        }

        v.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logoutUser(context!!)
            Toast.makeText(context, "Logged out!", Toast.LENGTH_SHORT).show()
            //TODO: go back to login screen; maybe just create a new fragment and just show it rather than cascading down through fragments
        }

        return v
    }
}