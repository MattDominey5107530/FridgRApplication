package com.example.fridgr

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import api.Diet
import api.Intolerance
import com.example.fridgr.local_storage.*

class ProfileFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): ProfileFragment =
            ProfileFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private lateinit var profilePictureImageView: ImageView

    /**
     * Fragment instantiation
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_profile, container, false)

        //Get reference to all components
        profilePictureImageView = v.findViewById(R.id.imvProfilePicture)

        if (isUserLoggedIn(context!!)) {
            updateProfilePicture()
            //TODO: Add different diet and cuisine icons to the recycler views
        } else {
            //Show loginFragment
            val loginFragment = LoginFragment.newInstance(switchToFragment, this)
            switchToFragment(this, loginFragment)
        }

        //TODO: Add edit button for name and profile picture (perhaps use Google profile picture as default if we allow them to login via Google)

        return v
    }

    /**
     * Function which gets the profile picture from local storage and adds it to the fragment.
     */
    private fun updateProfilePicture() {
        //Get the profile picture, if there is one
        val profilePicture = getProfilePicture(context!!)
        if (profilePicture != null) {
            val profilePictureDrawable = BitmapDrawable(context!!.resources, profilePicture)
            profilePictureImageView.background = profilePictureDrawable
        }
    }
}
