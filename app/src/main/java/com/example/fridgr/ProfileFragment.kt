package com.example.fridgr

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox

class ProfileFragment : Fragment() {

    val healthAttrs = arrayOf(
        "vegetarian",
        "pecatarian",
        "paleo",
        "vegan",
        "red-meat-free",
        "pork-free",
        "fish-free",
        "kosher",
        "gluten-free",
        "wheat-free",
        "dairy-free",
        "egg-free",
        "shellfish-free",
        "crustacean-free",
        "soy-free",
        "mustard-free",
        "lupine-free",
        "kidney-friendly",
        "keto-friendly"
        )
    val dietAttrs = arrayOf(
        "balanced",
        "low-potassium",
        "sugar-conscious",
        "low-sugar",
        "No-oil-added",
        "high-fiber",
        "high-protein",
        "low-carb",
        "low-fat",
        "low-sodium",
        "fodmap-free"
        )

    lateinit var drCheckBoxes: ArrayList<CheckBox>
    lateinit var dpCheckBoxes: ArrayList<CheckBox>

    /**
     * Fragment instantiation
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_profile, container, false)

        //Get reference to checkboxes to check state
        val tempDrArrayList = ArrayList<CheckBox>()
        for (i in 1..19) {
            tempDrArrayList.add(v.findViewById(resources.getIdentifier("chb_dr_attr$i", "id", "com.example.fridgr")) as CheckBox)
            tempDrArrayList[i-1].setOnClickListener { onClickCheckBox() }
        }
        drCheckBoxes = tempDrArrayList

        val tempDpArrayList = ArrayList<CheckBox>()
        for (i in 1..11) {
            tempDpArrayList.add(v.findViewById(resources.getIdentifier("chb_dp_attr$i", "id", "com.example.fridgr")) as CheckBox)
            tempDpArrayList[i-1].setOnClickListener { onClickCheckBox() }
        }
        dpCheckBoxes = tempDpArrayList

        //TODO: Add edit button for name and profile picture (perhaps use Google profile picture as default if we allow them to login via Google)

        return v
    }

    /**
     * Runs everytime a checkbox is clicked in the list
     */
    fun onClickCheckBox() {
        //TODO: Add functionality to save dietary information to local file
    }

    /**
     * Gets the attrbutes that are checked and returns an ArrayList containing the API key-strings which are checked the in the list
     */
    fun getCheckedAttributes(): ArrayList<String> {
        val checkedAttributes = arrayListOf<String>()
        for (i in 0..18) {
            if (drCheckBoxes[i].isChecked) checkedAttributes.add(healthAttrs[i])
        }

        for (i in 0..10) {
            if (dpCheckBoxes[i].isChecked) checkedAttributes.add(dietAttrs[i])
        }
        return checkedAttributes
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}
