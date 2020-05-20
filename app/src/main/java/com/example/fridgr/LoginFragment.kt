package com.example.fridgr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import api.Cuisine
import api.Diet
import api.Intolerance
import com.example.fridgr.local_storage.UserPreferences
import com.example.fridgr.local_storage.writeUserCuisines
import com.example.fridgr.local_storage.writeUserPreferences
import com.example.fridgr.local_storage.writeUserToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import user_database.UserDatabaseHandler

class LoginFragment: Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(switchToFragment: (Fragment, Fragment) -> Unit,
                        parentFragment: Fragment? = null): LoginFragment =
            LoginFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    /**
     * Fragment instantiation
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_login, container, false)

        //Couple the buttons with their button press functions
        val loginButton: Button = v.findViewById(R.id.btnLogin)
        loginButton.setOnClickListener { onClickLoginButton() }

        val registerButton: Button = v.findViewById(R.id.btnRegister)
        registerButton.setOnClickListener { onClickRegisterButton() }

        val forgottenPasswordButton: Button = v.findViewById(R.id.btnForgottenPassword)
        forgottenPasswordButton.setOnClickListener { onClickForgottenPasswordButton() }

        //Get reference to the EditTexts for username and password
        usernameEditText = v.findViewById(R.id.edtUsername)
        passwordEditText = v.findViewById(R.id.edtPassword)

        //TODO: Use as guest?

        return v
    }

    /**
     * Button press functions
     */
    private fun onClickLoginButton() {
        hideKeyboard()
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (username != "") {
            if (password != "") {
                CoroutineScope(IO).launch {
                    val userToken: String? = UserDatabaseHandler.authenticate(username, password)
                    if (userToken != null) {
                        writeUserToken(context!!, userToken)
                        val userPreferences = UserDatabaseHandler.getUserPreferences(userToken)
                        if (userPreferences != null) {
                            writeUserPreferences(context!!, userPreferences)
                        }
                        val cuisines = UserDatabaseHandler.getUserCuisines(userToken)
                        if (cuisines != null) {
                            writeUserCuisines(context!!, cuisines)
                        }
                        withContext(Main) {
                            updateFieldsAndSwitchBackToParent()
                        }
                    } else {
                        withContext(Main) {
                            Toast.makeText(context, "No combination of these credentials exists!.", Toast.LENGTH_SHORT).show()
                        }

                    }
                }



            } else {
                Toast.makeText(context, "Password required.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Username required.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFieldsAndSwitchBackToParent() {
        (myParentFragment!! as ProfileFragment).updateFields()
        switchToFragment(this, myParentFragment!!)
    }

    private fun onClickRegisterButton() {
        hideKeyboard()
        val registerFragment = RegisterFragment.newInstance(switchToFragment, this)
        switchToFragment(this, registerFragment)
    }

    private fun onClickForgottenPasswordButton() {
        //TODO: Implement with the full app; needs some infrastructure
        // e.g. a mail server to send forgotten password links etc.
    }
}
