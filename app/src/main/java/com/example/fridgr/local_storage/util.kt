package com.example.fridgr.local_storage

import android.content.Context
import android.graphics.Bitmap
import api.Diet
import api.Intolerance
import api.Recipe
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.BindException

data class UserPreferences(
    val intolerances: List<Intolerance>,
    val diet: Diet?
)

const val userPreferencesFileName = "user_preferences.txt"
const val userTokenFileName = "device_token.txt"
const val profilePictureFilename = "profile_picture.jpg"

fun isUserLoggedIn(context: Context): Boolean {
    val userPreferencesFile = context.getFileStreamPath(userPreferencesFileName)
    val deviceTokenFile = context.getFileStreamPath(userTokenFileName)

    val Aexists = userPreferencesFile.exists()
    val Bexists = deviceTokenFile.exists()

    return userPreferencesFile.exists() && deviceTokenFile.exists()
}

/**
 * Helper function to simplify getting text from a file.
 */
private fun getTextFromFile(context: Context, fileName: String): String? {
    return try {
        val fileInputStream = context.openFileInput(fileName)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder = StringBuilder()
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            stringBuilder.append(text)
        }
        stringBuilder.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Helper function to simplify writing text to a file.
 */
private fun writeTextToFile(context: Context, fileName: String, text: String) {
    try {
        val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutputStream.write(text.toByteArray())
        fileOutputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getUserToken(context: Context): String? {
    return getTextFromFile(context, userTokenFileName)
}

fun writeUserToken(context: Context, deviceToken: String) {
    writeTextToFile(context, userTokenFileName, deviceToken)
}

/**
 * Gets the UserPreferences from the file where the file is in the format:
 *  "INTOLERANCE,INTOLERANCE,INTOLERANCE-DIET"
 */
fun getUserPreferences(context: Context): UserPreferences? {
    val userPreferenceFileString = getTextFromFile(context, userPreferencesFileName)
    if (userPreferenceFileString != null) {
        val intolerances: List<Intolerance>
        val diet: Diet?

        //Split up the string into intolerances and diet
        val (intolerancesStrings, dietString) =
            userPreferenceFileString.split("-")

        //Extract the intolerance enums, if there are any
        intolerances = if (intolerancesStrings != "") {
            val intoleranceStringList: List<String> = intolerancesStrings.split(",")
            intoleranceStringList.map { intoleranceString ->
                Intolerance.valueOf(intoleranceString)
            }
        } else {
            emptyList()
        }

        //Extract the diet, if there is one
        diet = if (dietString != "null") {
            Diet.valueOf(dietString)
        } else {
            null
        }

        return UserPreferences(intolerances, diet)
    } else {
        return null
    }
}

/**
 * Converts the UserPreference to text and writes it to the file in a format that
 *  getUserPreferences can read
 */
fun writeUserPreferences(context: Context, userPreferences: UserPreferences) {
    val intoleranceStrings: List<String> =
        userPreferences.intolerances.map { intolerance ->
            intolerance.name
        }
    val intolerancesString: String = intoleranceStrings.joinToString(",")
    val dietString: String? = userPreferences.diet?.name

    val userPreferencesString = "$intolerancesString-$dietString"
    writeTextToFile(context, userPreferencesFileName, userPreferencesString)
}

fun writeUserPreferences(context: Context, intolerances: List<Intolerance>, diet: Diet?) {
    writeUserPreferences(context, UserPreferences(intolerances, diet))
}

fun getProfilePicture(context: Context): Bitmap? {
    return TODO("Not implemented")
}

fun writeProfilePicture(context: Context, profilePicture: Bitmap) {

}

fun getFavouriteRecipes(context: Context): List<Recipe> {
    return TODO("Not implemented")
}

fun writeFavouriteRecipes(context: Context, recipeList: List<Recipe>) {

}