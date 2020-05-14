package com.example.fridgr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class RegisterFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): RegisterFragment =
            RegisterFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    /**
     * Fragment instantiation
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_register, container, false)



        return v
    }


}