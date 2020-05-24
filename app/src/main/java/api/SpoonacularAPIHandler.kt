package api

import android.content.Context
import com.example.fridgr.RecipeSearchFragment
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import okhttp3.*
import user_database.UserDatabaseHandler
import user_database.writeCurrentUserQuery
import java.io.IOException


object SpoonacularAPIHandler : ISpoonacularAPIHandler {

    private const val apiKey = "161c30243b094154ad28c32033413409"

    lateinit var localStorageContext: Context

    // Pass in list of ingredients, return a list of recipes including those ingredients.
    override suspend fun getRecipeListByIngredients(
        ingredients: List<Ingredient>
    ): List<IngredientSearchRecipe> {

        // Recipes to be returned
        var recipeTestIngredients: List<TestIngredientSearchRecipe> = emptyList()

        // Set default values, can pass these in as parameters and get input from app in the future.
        val recipeCount = 10
        val licensing = true
        val ranking = 2
        val ignorePantry = true

        // Build URL
        var ingredientString = ""
        for (ingredient in ingredients) {
            if (ingredient === ingredients.last()) {
                ingredientString += ingredient.name
            } else {
                ingredientString = ingredientString + ingredient.name + ","
            }
        }

        val url = "https://api.spoonacular.com/recipes/findByIngredients?" +
                "apiKey=$apiKey" +
                "&ingredients=$ingredientString" +
                "&number=$recipeCount" +
                "&ranking=$ranking" +
                "&ignorePantry=$ignorePantry" +
                "&limitLicense=$licensing"

        UserDatabaseHandler.writeCurrentUserQuery(localStorageContext, url.replace(apiKey, "<ApiKeyOmitted>"))

        var stillWaitingForResponse = true

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                recipeTestIngredients =
                    gson.fromJson(body, Array<TestIngredientSearchRecipe>::class.java).toList()

                stillWaitingForResponse = false
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                stillWaitingForResponse = false
            }
        })

        //Waits until a response is received.
        while (stillWaitingForResponse) {
            delay(100)
        }

        //Convert from TestIngredientSearchRecipe to IngredientSearchRecipe to match UI code
        return recipeTestIngredients.map { it.toIngredientSearchRecipe() }
    }

    // Search for recipes using a string with other filters, returns list of recipes.
    override suspend fun getRecipeListBySearch(
        recipeSearchText: String,
        intolerances: List<Intolerance>,
        diet: Diet?,
        cuisines: List<Cuisine>,
        mealType: MealType?,
        nutritionFilters: RecipeSearchFragment.NutritionFilters?
    ): List<Recipe> {

        var search: TestSearch
        var recipeList: List<TestSearchRecipe> = emptyList()

        // Default Values
        val recipeCount = 20
        val licensing = true
        val instructionsRequired = true

        val carbRange = nutritionFilters?.carbRange
        val proteinRange = nutritionFilters?.proteinRange
        val fatRange = nutritionFilters?.fatRange
        val caloryRange = nutritionFilters?.caloryRange

        val dietString = if (diet != null) "&diet=${getDietStringFromDiet(diet)}" else ""
        val mealTypeString =
            if (mealType != null) "&type=${getMealTypeStringFromMealType(mealType)}" else ""

        var cuisinesString = ""
        var intolerancesString = ""

        for (cuisine in cuisines) {
            if (cuisine === cuisines.last()) {
                cuisinesString += getCuisineStringFromCuisine(cuisine)
            } else {
                cuisinesString = "$cuisinesString${getCuisineStringFromCuisine(cuisine)},"
            }

        }

        for (intolerance in intolerances) {
            if (intolerance === intolerances.last()) {
                intolerancesString += getIntoleranceStringFromIntolerance(intolerance)
            } else {
                intolerancesString =
                    "$intolerancesString${getIntoleranceStringFromIntolerance(intolerance)},"
            }

        }

        val url = "https://api.spoonacular.com/recipes/complexSearch?" +
                "apiKey=$apiKey" +
                "&query=${recipeSearchText.replace(" ", "-")}" +
                "&cuisine=$cuisinesString" +
                "&intolerances=$intolerancesString" +
                dietString +
                (if (carbRange != null) "&minCarbs=${carbRange.min()
                    .toString()}" else "&minCarbs=0") +
                (if (proteinRange != null) "&minProtein=${proteinRange.min()
                    .toString()}" else "&minProtein=0") +
                (if (fatRange != null) "&minFat=${fatRange.min()
                    .toString()}" else "&minFat=0") +
                (if (caloryRange != null) "&minCalories=${caloryRange.min()
                    .toString()}" else "&minCalories=0") +
                (if (carbRange != null) "&maxCarbs=${carbRange.max()
                    .toString()}" else "") +
                (if (proteinRange != null) "&maxProtein=${proteinRange.max()
                    .toString()}" else "") +
                (if (fatRange != null) "&maxFat=${fatRange.max()
                    .toString()}" else "") +
                (if (caloryRange != null) "&maxCalories=${caloryRange.max()
                    .toString()}" else "") +
                mealTypeString +
                "&number=$recipeCount" +
                "&limitLicense=$licensing" +
                "&sort=popularity" +
                "&instructionRequired=$instructionsRequired"

        UserDatabaseHandler.writeCurrentUserQuery(localStorageContext, url.replace(apiKey, "<ApiKeyOmitted>"))

        var stillWaitingForResponse = true

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                search = gson.fromJson(body, TestSearch::class.java)
                recipeList = search.results

                stillWaitingForResponse = false
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                stillWaitingForResponse = false
            }
        })

        //Waits until a response is received.
        while (stillWaitingForResponse) {
            delay(100)
        }

        return recipeList.map { it.toRecipe() }
    }

    // Takes in a recipe and returns all its details
    override suspend fun getRecipeInfo(
        recipeId: Int
    ): RecipeInfo? {
        var moreInfo: RecipeInfo? = null

        val includeNutrition = false
        val url = "https://api.spoonacular.com/recipes/$recipeId/information?" +
                "apiKey=$apiKey" +
                "&includeNutrition=$includeNutrition"

        var stillWaitingForResponse = true

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                moreInfo = gson.fromJson(body, RecipeInfo::class.java)

                stillWaitingForResponse = false
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                stillWaitingForResponse = false
            }
        })

        //Waits until a response is received.
        while (stillWaitingForResponse) {
            delay(100)
        }

        return moreInfo
    }

    // Takes in a recipe and returns its method.
    override suspend fun getRecipeInstructions(
        recipeId: Int
    ): List<RecipeInstructions> {
        var recipeInstructions: List<RecipeInstructions> = emptyList()

        val url = "https://api.spoonacular.com/recipes/$recipeId/analyzedInstructions?" +
                "apiKey=$apiKey"

        var stillWaitingForResponse = true

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                recipeInstructions =
                    gson.fromJson(body, Array<RecipeInstructions>::class.java).toList()

                stillWaitingForResponse = false
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                stillWaitingForResponse = false
            }
        })

        //Waits until a response is received.
        while (stillWaitingForResponse) {
            delay(100)
        }

        return recipeInstructions
    }

    override suspend fun getAutocompletedIngredientList(
        ingredientSearchText: String,
        intolerances: List<Intolerance>
    ): List<Ingredient> {

        var autoCompleteIngredientResult: List<TestIngredient> = emptyList()

        val ingredientCount = 15
        var intolerancesString = ""

        for (intolerance in intolerances) {
            if (intolerance === intolerances.last()) {
                intolerancesString += intolerance.toString()
            } else {
                intolerancesString = "$intolerancesString$intolerance,"
            }
        }

        val url = "https://api.spoonacular.com/food/ingredients/autocomplete?" +
                "apiKey=$apiKey" +
                "&query=$ingredientSearchText" +
                "&number=$ingredientCount" +
                "&metaInformation=true"

        var stillWaitingForResponse = true

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                autoCompleteIngredientResult =
                    gson.fromJson(body, Array<TestIngredient>::class.java).toList()

                stillWaitingForResponse = false
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                stillWaitingForResponse = false
            }
        })

        //Waits until a response is received.
        while (stillWaitingForResponse) {
            delay(100)
        }

        return autoCompleteIngredientResult.map { it.toIngredient() }
    }

    override suspend fun getAutocompletedRecipeList(
        recipeSearchText: String
    ): List<TestSearchRecipe> {

        var autoCompleteRecipeResult: List<TestSearchRecipe> = emptyList()

        val number = 5
        val url = "https://api.spoonacular.com/recipes/autocomplete?" +
                "apiKey=$apiKey" +
                "&number=$number" +
                "&query=$recipeSearchText"

        var stillWaitingForResponse = true

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                autoCompleteRecipeResult =
                    gson.fromJson(body, Array<TestSearchRecipe>::class.java).toList()

                stillWaitingForResponse = false
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                stillWaitingForResponse = false
            }
        })

        //Waits until a response is received.
        while (stillWaitingForResponse) {
            delay(100)
        }

        return autoCompleteRecipeResult
    }

    // Takes in a recipe, and returns similar ones.
    override suspend fun getSimilarRecipeList(
        id: Int
    ): List<Recipe> {

        var similarRecipes: List<TestSearchRecipe> = emptyList()

        val recipeCount = 10
        val limitLicence = true

        val url = "https://api.spoonacular.com/recipes/$id/similar?" +
                "apiKey=$apiKey" +
                "&number=$recipeCount" +
                "&limitLicence=$limitLicence"

        UserDatabaseHandler.writeCurrentUserQuery(localStorageContext, url.replace(apiKey, "<ApiKeyOmitted>"))

        var stillWaitingForResponse = true

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                similarRecipes = gson.fromJson(body, Array<TestSearchRecipe>::class.java).toList()

                stillWaitingForResponse = false
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                stillWaitingForResponse = false
            }
        })

        //Waits until a response is received.
        while (stillWaitingForResponse) {
            delay(100)
        }

        return similarRecipes.map { it.toRecipe() }
    }
}