package com.example.fridgr.local_storage

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import api.*
import com.example.fridgr.R
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

data class UserPreferences(
    val intolerances: List<Intolerance>,
    val diet: Diet?
)

const val userPreferencesFileName = "user_preferences.txt"
const val userCuisineFileName = "user_cuisines.txt"
const val userTokenFileName = "device_token.txt"
const val userFavouritesFilename = "user_favourites.txt"
const val profilePictureFilename = "profile_picture.jpg"

/**
 * TODO temp: Called every time the app runs so that there are no files left over.
 */
fun logoutUser(context: Context) {
    context.deleteFile(userPreferencesFileName)
    context.deleteFile(userTokenFileName)
    context.deleteFile(userCuisineFileName)
    context.deleteFile(profilePictureFilename)
}

/**
 * Checks whether the user is logged in by checking whether the deviceTokenFile exists.
 */
fun isUserLoggedIn(context: Context): Boolean {
    val deviceTokenFile = context.getFileStreamPath(userTokenFileName)
    return deviceTokenFile.exists()
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
        //e.printStackTrace()
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
        //e.printStackTrace()
    }
}

private fun getBitmapFromFile(context: Context, filename: String): Bitmap? {
    return try {
        val fileInputStream = context.openFileInput(filename)
        BitmapFactory.decodeStream(fileInputStream)
    } catch (e: Exception) {
        //e.printStackTrace()
        null
    }
}

private fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String) {
    val wrapper = ContextWrapper(context)
    //val bitmapFile = wrapper.getDir("Images", Context.MODE_PRIVATE)
    val bitmapFile = File(
        wrapper.getDir("Images", Context.MODE_PRIVATE),
        filename
    )

    try {
        val fileOutputStream = FileOutputStream(bitmapFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    } catch (e: Exception) {
        //e.printStackTrace()
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
 *  "INTOLERANCE,INTOLERANCE,INTOLERANCE-DIET-CUISINE,CUISINE,CUISINE"
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
 *  getUserPreferences can read.
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

fun writeUserPreferences(
    context: Context,
    intolerances: List<Intolerance>,
    diet: Diet?
) {
    writeUserPreferences(context, UserPreferences(intolerances, diet))
}

fun getUserCuisines(context: Context): List<Cuisine>? {
    val userCuisineString = getTextFromFile(context, userCuisineFileName)
    return if (userCuisineString != null) {
        //Extract the cuisine, if there are any
        if (userCuisineString != "") {
            val cuisineStringList: List<String> = userCuisineString.split(",")
            cuisineStringList.map { cuisineString ->
                Cuisine.valueOf(cuisineString)
            }
        } else {
            null
        }
    } else {
        null
    }
}

fun writeUserCuisines(context: Context, cuisines: List<Cuisine>) {
    val cuisineStrings: List<String> =
        cuisines.map { cuisine ->
            cuisine.name
        }
    val cuisineString: String = cuisineStrings.joinToString(",")
    writeTextToFile(context, userCuisineFileName, cuisineString)
}

/**
 * Gets the user's favourite recipes from the local storage in the format:
 *  ID,NAME,NUTRITION_NAME~NUTRITION_VALUE~NUTRITION_UNIT;NUTRITION_NAME~NUTRITION_VALUE~NUTRITION_UNIT,IMAGE_STRING
 *  ID,NAME,NUTRITION_NAME~NUTRITION_VALUE~NUTRITION_UNIT;NUTRITION_NAME~NUTRITION_VALUE~NUTRITION_UNIT,IMAGE_STRING etc.
 */
fun getFavouriteRecipes(context: Context): List<Recipe>? {
    val userFavouritesFileStrings = getTextFromFile(context, userFavouritesFilename)
    if (userFavouritesFileStrings != null) {
        val favouriteRecipeStringList = userFavouritesFileStrings.split("\n")

        val recipeList = arrayListOf<Recipe>()
        for (favouriteRecipeString in favouriteRecipeStringList) {
            val (idString, nameString, nutritionsString, imageString) =
                favouriteRecipeString.split(",")

            val nutritionStringList = nutritionsString.split(';')
            val nutritionList = arrayListOf<Nutrition>()
            for (nutritionString in nutritionStringList) {
                val splitNutritionString = nutritionString.split("~")
                nutritionList.add(
                    Nutrition(
                        splitNutritionString[0],
                        splitNutritionString[1].toDouble(),
                        splitNutritionString[2]
                    )
                )
            }

            recipeList.add(
                Recipe(
                    idString.toInt(),
                    nameString,
                    nutritionList,
                    imageString
                )
            )
        }
        return recipeList.toList()
    } else {
        return null
    }
}

/**
 * Writes the user's favourite recipes to file
 */
fun writeFavouriteRecipes(context: Context, recipeList: List<Recipe>) {
    fun getNutritionString(nutrition: Nutrition) =
        "${nutrition.name}~${nutrition.value}~${nutrition.unit}"

    val recipeStrings: List<String> = recipeList.map { recipe ->
        "${recipe.id.toString()},${recipe.name},${recipe.nutritionList.joinToString(";") {
            getNutritionString(it)
        }},${recipe.imageString}"
    }
    val recipesString = recipeStrings.joinToString("\n")

    writeTextToFile(context, userFavouritesFilename, recipesString)
}

fun getProfilePicture(context: Context): Bitmap? {
    return getBitmapFromFile(context, profilePictureFilename)
}

fun writeProfilePicture(context: Context, profilePicture: Bitmap) {
    saveBitmapToFile(context, profilePicture, profilePictureFilename)
}