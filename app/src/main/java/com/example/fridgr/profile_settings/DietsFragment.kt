package com.example.fridgr.profile_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import api.Diet
import api.Intolerance
import com.example.fridgr.R
import com.example.fridgr.local_storage.UserPreferences
import com.example.fridgr.local_storage.getUserPreferences
import com.example.fridgr.local_storage.isUserLoggedIn
import com.example.fridgr.local_storage.writeUserPreferences

class DietsFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): DietsFragment =
            DietsFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private val intoleranceCheckBoxes = ArrayList<CheckBox>()
    private val dietCheckBoxes = ArrayList<CheckBox>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_profile_settings_diets, container, false)

        //Setup components
        v.findViewById<ImageButton>(R.id.imbBack)
            .setOnClickListener {
                switchToFragment(this, myParentFragment!!)
            }

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
                    /**
                     * Un-checks all other diet checkboxes then checks this one
                     *  so that there is only ever 1 diet selected.
                     */
                    val chbIsChecked = this.isChecked
                    uncheckAllDietCheckBoxes()
                    this.isChecked = chbIsChecked
                    onClickCheckBox()
                }
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