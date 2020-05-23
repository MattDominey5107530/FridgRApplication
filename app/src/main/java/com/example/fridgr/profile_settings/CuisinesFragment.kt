package com.example.fridgr.profile_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import api.Cuisine
import api.Diet
import api.Intolerance
import com.example.fridgr.R
import com.example.fridgr.local_storage.*

class CuisinesFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): CuisinesFragment =
            CuisinesFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private val cuisineCheckBoxes = arrayListOf<CheckBox>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_profile_settings_cuisines, container, false)

        //Setup components
        v.findViewById<ImageButton>(R.id.imbBack)
            .setOnClickListener {
                switchToFragment(this, myParentFragment!!)
            }

        for (i in 0 until 26) {
            cuisineCheckBoxes.add(
                v.findViewById(
                    resources.getIdentifier(
                        "chbCuisine$i",
                        "id",
                        "com.example.fridgr"
                    )
                ) as CheckBox
            )
            with(cuisineCheckBoxes[i]) {
                setOnClickListener { onClickCheckBox() }
            }
        }

        populateFields()

        return v
    }

    /**
     * Set the checked state of the checkboxes to that of the actual user settings
     */
    private fun populateFields() {
        if (isUserLoggedIn(context!!)) {
            //Gets the user preferences from
            val cuisines: List<Cuisine>? = getUserCuisines(context!!)
            if (cuisines != null) {
                for (i in 0 until 26) {
                    if (Cuisine.values()[i] in cuisines) {
                        cuisineCheckBoxes[i].isChecked = true
                    }
                }
            }
        }
    }

    /**
     * Saves the cuisines after each checkbox click
     */
    private fun onClickCheckBox() {
        writeUserCuisines(context!!, getCheckedCuisines())
    }

    /**
     * Gets the list of cuisines from the checkbox states
     */
    private fun getCheckedCuisines(): List<Cuisine> {
        //Get all the intolerances
        val checkedCuisines = arrayListOf<Cuisine>()
        for (i in 0 until 26) {
            if (cuisineCheckBoxes[i].isChecked) checkedCuisines.add(Cuisine.values()[i])
        }

        //Return an object ready to be written to file (local_storage/util.kt::writeUserPreferences)
        return checkedCuisines
    }
}