package com.example.fridgr.profile_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.fridgr.R

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

        return v
    }
}