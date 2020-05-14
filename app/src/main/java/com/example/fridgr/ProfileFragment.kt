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

    private val intoleranceCheckBoxes = ArrayList<CheckBox>()
    private val dietCheckBoxes = ArrayList<CheckBox>()
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
        for (i in 0 until 12) {
            intoleranceCheckBoxes.add(
                v.findViewById(
                    resources.getIdentifier(
                        "chb_intolerance$i",
                        "id",
                        "com.example.fridgr"
                    )
                ) as CheckBox
            )
            with(intoleranceCheckBoxes[i]) {
                setOnClickListener { onClickCheckBox() }
            }
        }
        for (i in 0 until 10) {
            dietCheckBoxes.add(
                v.findViewById(
                    resources.getIdentifier(
                        "chb_diet$i",
                        "id",
                        "com.example.fridgr"
                    )
                ) as CheckBox
            )
            with(dietCheckBoxes[i]) {
                setOnClickListener {
                    //Unchecks all other diet checkboxes then checks this one so that there is only ever 1 diet selected.
                    val chbIsChecked = this.isChecked
                    uncheckAllDietCheckBoxes()
                    this.isChecked = chbIsChecked
                    onClickCheckBox()
                }
            }
        }
        if (isUserLoggedIn(context!!)) {
            populateFields()
        } else {
            //Show loginFragment
            val loginFragment = LoginFragment.newInstance(switchToFragment, this)
            switchToFragment(this, loginFragment)
        }


        //TODO: Add edit button for name and profile picture (perhaps use Google profile picture as default if we allow them to login via Google)

        return v
    }

    fun populateFields() {
        if (isUserLoggedIn(context!!)) {
            //Gets the user preferences from
            val userPreferences: UserPreferences? = getUserPreferences(context!!)
            if (userPreferences != null) {
                for (i in 0 until 12) {
                    if (Intolerance.values()[i] in userPreferences.intolerances) {
                        intoleranceCheckBoxes[i].isChecked = true
                    }
                }
                for (i in 0 until 10) {
                    if (Diet.values()[i] == userPreferences.diet) {
                        dietCheckBoxes[i].isChecked = true
                    }
                }
            }
            //Get the profile picture, if there is one
            val profilePicture = getProfilePicture(context!!)
            if (profilePicture != null) {
                val profilePictureDrawable = BitmapDrawable(context!!.resources, profilePicture)
                profilePictureImageView.background = profilePictureDrawable
            }
        }
    }

    private fun uncheckAllDietCheckBoxes() {
        dietCheckBoxes.forEach { it.isChecked = false }
    }

    /**
     * Saves the preferences after each checkbox click
     */
    private fun onClickCheckBox() {
        writeUserPreferences(context!!, getCheckedPreferences())
    }

    /**
     * Gets the UserPreferences from the checkbox states
     */
    private fun getCheckedPreferences(): UserPreferences {
        //Get all the intolerances
        val checkedIntolerances = arrayListOf<Intolerance>()
        for (i in 0 until 12) {
            if (intoleranceCheckBoxes[i].isChecked) checkedIntolerances.add(Intolerance.values()[i])
        }

        //Get their diet
        var diet: Diet? = null
        for (i in 0 until 10) {
            if (dietCheckBoxes[i].isChecked) {
                diet = Diet.values()[i]
                break //There should only be 1 diet checked
            }
        }

        //Return an object ready to be written to file (local_storage/util.kt::writeUserPreferences)
        return UserPreferences(checkedIntolerances, diet)
    }
}
