package com.example.fridgr.profile_settings

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import api.Diet
import api.Intolerance
import com.example.fridgr.GDPRDataRequestFragment
import com.example.fridgr.LoginFragment
import com.example.fridgr.ProfileFragment
import com.example.fridgr.R
import com.example.fridgr.local_storage.*
import com.example.fridgr.popups.EditNicknamePopup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import user_database.UserDatabaseHandler
import user_database.deleteAllUserData
import user_database.requestAllUserData

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

        //Show sub buttons of Edit Profile: "Change profile picture" & "Change nickname"
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

        val requestDataButton = v.findViewById<Button>(R.id.btnRequestData)
        val deleteAccountDataButton = v.findViewById<Button>(R.id.btnDeleteAccountData)
        requestDataButton.visibility = View.GONE
        deleteAccountDataButton.visibility = View.GONE

        //Show sub buttons of GDPR: "Request data" & "Delete data"
        v.findViewById<Button>(R.id.btnGDPR).setOnClickListener {
            if (requestDataButton.visibility == View.VISIBLE) {
                requestDataButton.visibility = View.GONE
                deleteAccountDataButton.visibility = View.GONE
            } else {
                requestDataButton.visibility = View.VISIBLE
                deleteAccountDataButton.visibility = View.VISIBLE
            }
        }

        requestDataButton.setOnClickListener {
            CoroutineScope(IO).launch {
                val userToken = getUserToken(context!!)
                if (userToken != null) {
                    val userDataString = UserDatabaseHandler.requestAllUserData(userToken)
                    withContext(Main) {
                        if (userDataString != null) {
                            val gdprDataRequestFragment = GDPRDataRequestFragment.newInstance(
                                switchToFragment,
                                this@ProfileSettingsFragment,
                                userDataString
                            )
                            switchToFragment(this@ProfileSettingsFragment, gdprDataRequestFragment)
                        } else {
                            Toast.makeText(
                                context!!,
                                "An error occurred whilst trying to communicate with the database.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context!!,
                        "An error occurred whilst trying to communicate with the database.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        deleteAccountDataButton.setOnClickListener {
            showDeleteDataYesNoDialog()
        }

        v.findViewById<Button>(R.id.btnMembershipSettings).setOnClickListener {
            //TODO: Implement with full app; link the app to Google Play so that users can buy
            // a membership with the app and manage that membership in this tab.
        }

        v.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logoutUser(context!!)
            Toast.makeText(context, "Logged out!", Toast.LENGTH_SHORT).show()
            val loginFragment = LoginFragment.newInstance(switchToFragment)
            switchToFragment(this, loginFragment)
        }

        return v
    }

    /**
     * Helper function to show a YesNo dialog box to confirm whether the user wants to delete
     *  their account.
     */
    private fun showDeleteDataYesNoDialog() {
        lateinit var dialog: AlertDialog

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    CoroutineScope(IO).launch {
                        val userToken = getUserToken(context!!)
                        if (userToken != null) {
                            UserDatabaseHandler.deleteAllUserData(userToken)
                            deleteAllUserData(context!!)

                            withContext(Main) {
                                Toast.makeText(
                                    context!!,
                                    "Your data has been removed. Logging out...",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val loginFragment = LoginFragment.newInstance(switchToFragment)
                                switchToFragment(this@ProfileSettingsFragment, loginFragment)
                            }
                        } else {
                            withContext(Main) {
                                Toast.makeText(
                                    context!!,
                                    "An error occurred whilst trying to communicate with the database.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                } //Do nothing just dismiss
            }
        }

        val builder = AlertDialog.Builder(context!!).apply {
            setTitle("Deleting Account")
            setMessage(
                "Are you REALLY sure you want to delete all your user information?\n" +
                        "(This operation cannot be undone)"
            )
            setPositiveButton("YES", dialogClickListener)
            setNegativeButton("NO", dialogClickListener)
        }

        dialog = builder.create()
        dialog.show()
    }

    /**
     * @Author Hasangi Kahaduwa
     * Function to display menu which lets the user get an image from taking a photo or
     *  selecting one from their gallery. (modified)
     */
    private fun pickProfilePicture(context: Context) {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        AlertDialog.Builder(context).apply {
            setTitle("Choose your profile picture:")
            setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val takePicture =
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePicture, 0)
                    }
                    1 -> {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) -> {
                                val pickPhoto = Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                                startActivityForResult(pickPhoto, 1)
                            }
                            else -> {
                                // You can directly ask for the permission.
                                requestPermissions(
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    1
                                )
                            }
                        }
                    }
                    2 -> {
                        dialog!!.dismiss()
                    }
                }
            }
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

                                var profilePictureBitmap =
                                    BitmapFactory.decodeFile(picturePath)

                                if (profilePictureBitmap != null) {
                                    writeProfilePicture(
                                        context!!,
                                        profilePictureBitmap
                                    )
                                }

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