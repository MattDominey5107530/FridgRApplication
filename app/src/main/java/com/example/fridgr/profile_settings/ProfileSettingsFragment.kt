package com.example.fridgr.profile_settings

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.Diet
import api.Intolerance
import com.example.fridgr.LoginFragment
import com.example.fridgr.ProfileFragment
import com.example.fridgr.R
import com.example.fridgr.local_storage.*
import com.example.fridgr.popups.EditNicknamePopup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import user_database.UserDatabaseHandler

class ProfileSettingsFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): ProfileSettingsFragment =
            ProfileSettingsFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_profile_settings, container, false)

        //Set the onClicks
        v.findViewById<ImageButton>(R.id.imbBack).setOnClickListener {
            (myParentFragment as ProfileFragment).updateFields()
            switchToFragment(this, myParentFragment!!)
            pushChangesToUserDatabase()
        }

        v.findViewById<Button>(R.id.btnDietaryRequirements).setOnClickListener {
            val dietsFragment = DietsFragment.newInstance(switchToFragment, this)
            switchToFragment(this, dietsFragment)
        }

        v.findViewById<Button>(R.id.btnFavouriteCuisines).setOnClickListener {
            val cuisinesFragment = CuisinesFragment.newInstance(switchToFragment, this)
            switchToFragment(this, cuisinesFragment)
        }

        val changeProfilePictureButton = v.findViewById<Button>(R.id.btnChangeProfilePicture)
        val changeNicknameButton = v.findViewById<Button>(R.id.btnChangeNickname)
        changeProfilePictureButton.visibility = View.GONE
        changeNicknameButton.visibility = View.GONE

        v.findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
            if (changeProfilePictureButton.visibility == View.VISIBLE) {
                changeProfilePictureButton.visibility = View.GONE
                changeNicknameButton.visibility = View.GONE
            } else {
                changeProfilePictureButton.visibility = View.VISIBLE
                changeNicknameButton.visibility = View.VISIBLE
            }
        }

        changeProfilePictureButton.setOnClickListener {
            pickProfilePicture(context!!)
        }

        changeNicknameButton.setOnClickListener {
            val userNickname = getUserNickname(context!!)
            val editNicknamePopup = EditNicknamePopup(context!!, {}, userNickname)
            editNicknamePopup.showAtLocation(
                v,
                Gravity.CENTER,
                0,
                0
            )
        }


        v.findViewById<Button>(R.id.btnMembershipSettings).setOnClickListener {
            //TODO: Implement with full app; link the app to Google Play so that users can buy
            // a membership with the app and manage that membership in this tab.
        }

        v.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logoutUser(context!!)
            Toast.makeText(context, "Logged out!", Toast.LENGTH_SHORT).show()
            val loginFragment = LoginFragment.newInstance(switchToFragment, this)
            switchToFragment(myParentFragment!!, loginFragment)
        }

        return v
    }

    private fun pickProfilePicture(context: Context) {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        val builder = AlertDialog.Builder(context).apply {
            setTitle("Choose your profile picture:")
            setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when (which) {
                        0 -> {
                            val takePicture =
                                Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(takePicture, 0)
                        }
                        1 -> {
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(pickPhoto, 1)
                        }
                        2 -> {
                            dialog!!.dismiss()
                        }
                    }
                }
            })
            show()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 ->
                    if (resultCode == RESULT_OK && data != null) {
                        val selectedImage: Bitmap = data.extras!!.get("data") as Bitmap
                        writeProfilePicture(context!!, selectedImage)
                    }
                1 ->
                    if (resultCode == RESULT_OK && data != null) {
                        val selectedImage: Uri? = data.data
                        val filePathColumn: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                        if (selectedImage != null) {
                            val cursor = context!!.contentResolver.query(
                                selectedImage,
                                filePathColumn,
                                null,
                                null,
                                null
                            )
                            if (cursor != null) {
                                cursor.moveToFirst()
                                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                                val picturePath = cursor.getString(columnIndex)
                                writeProfilePicture(
                                    context!!,
                                    BitmapFactory.decodeFile(picturePath)
                                )
                                cursor.close()
                            }
                        }
                    }
            }
        }
    }

    /**
     * Updates the user_database after making changes to the user's information in settings.
     */
    private fun pushChangesToUserDatabase() {
        val userPreferences = getUserPreferences(context!!)
        val cuisines = getUserCuisines(context!!)
        val userNickname = getUserNickname(context!!)

        val userToken = getUserToken(context!!)
        CoroutineScope(IO).launch {
            if (userToken != null) {
                with(UserDatabaseHandler) {
                    if (userPreferences != null) {
                        writeUserPreferences(userToken, userPreferences)
                    }
                    if (cuisines != null) {
                        writeUserCuisines(userToken, cuisines)
                    }
                    if (userNickname != "Default User") {
                        writeUserNickname(userToken, userNickname)
                    }
                }
            }
        }
    }
}