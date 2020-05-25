package com.example.fridgr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import user_database.UserDatabaseHandler
import user_database.register

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

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var password2EditText: EditText
    private lateinit var acceptGDPRCheckBox: CheckBox

    /**
     * Fragment instantiation
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_register, container, false)

        usernameEditText = v.findViewById(R.id.edtUsername)
        passwordEditText = v.findViewById(R.id.edtPassword)
        password2EditText = v.findViewById(R.id.edtReTypePassword)
        acceptGDPRCheckBox = v.findViewById(R.id.chbAcceptGDPR)

        v.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            register()
        }

        v.findViewById<ImageButton>(R.id.imbBack).setOnClickListener {
            switchToFragment(this@RegisterFragment, myParentFragment!!)
        }

        return v
    }

    private fun register() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val password2 = password2EditText.text.toString()
        val accepted = acceptGDPRCheckBox.isChecked

        if (accepted) {
            if (username != "") {
                if (UserDatabaseHandler.isTextLegal(username)) {
                    if (password != "") {
                        if (password == password2) {
                            CoroutineScope(IO).launch {
                                UserDatabaseHandler.register(username, password)
                                withContext(Main) {
                                    Toast.makeText(context!!, "Successfully registered.", Toast.LENGTH_SHORT).show()
                                    switchToFragment(this@RegisterFragment, myParentFragment!!)
                                }
                            }
                        } else {
                            Toast.makeText(
                                context!!,
                                "Your passwords do not match!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context!!, "Your password is not valid.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context!!, "Your username contains invalid characters.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context!!, "Your username cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                context!!,
                "You must accept the GDPR in order to register.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}