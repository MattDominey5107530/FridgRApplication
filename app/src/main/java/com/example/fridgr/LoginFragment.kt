package com.example.fridgr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class LoginFragment: Fragment() {

    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText

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

        /**
         * Get reference to the EditTexts for username and password
         */
        usernameEditText = v.findViewById(R.id.edtUsername)
        passwordEditText = v.findViewById(R.id.edtPassword)

        return v
    }

    companion object {
        fun newInstance(): LoginFragment = LoginFragment()
    }


    /**
     * Button press functions
     */
    private fun onClickLoginButton() {
        TODO("not implemented")
    }

    private fun onClickRegisterButton() {
        TODO("not implemented")
    }

    private fun onClickForgottenPasswordButton() {
        TODO("not implemented")
    }

    /**
     * GUI function TODO("not implemented")
     */

}
