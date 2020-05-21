package api

import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


object APIHandlerClass : ISpoonacularAPIHandler {

    private const val apiKey = "161c30243b094154ad28c32033413409"

    // Pass in list of ingredients, return a list of recipes including those ingredients.
    override fun getRecipeListByIngredients(
        ingredients: List<TestIngredient>
    ): List<TestIngredientSearchRecipe> {

        // Recipes to be returned
        var recipeTestIngredients : List<TestIngredientSearchRecipe> = emptyList()

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
            }
            else {
                ingredientString = ingredientString + ingredient.name + ",+"
            }
        }

        var url = "https://api.spoonacular.com/recipes/findByIngredients?" +
                "apiKey=$apiKey" +
                "&ingredients=$ingredientString" +
                "&number=$recipeCount" +
                "&ranking=$ranking" +
                "&ignorePantry=$ignorePantry" +
                "&limitLicense=$licensing"


        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println("Json String: $body")

                val gson = GsonBuilder().create()

                recipeTestIngredients = gson.fromJson(body, Array<TestIngredientSearchRecipe>::class.java).toList()


                println("Recipe List: $recipeTestIngredients")
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute")
            }
        })

        return recipeTestIngredients
    }

    // Search for recipes using a string with other filters, returns list of recipes.
    //Todo: Replace the strings with the enums or convert enums to string when passing it into function
    //Todo: Search will not work if one of the paramaters is not passed in, and it needs to be valid
    override fun getRecipeListBySearch(
        recipeSearchText: String,
        intolerances: List<String>,
        diets: String,
        cuisines: List<String>,
        mealTypes: String
    ): List<TestSearchRecipe> {

        var search: TestSearch
        var recipeList: List<TestSearchRecipe> = emptyList()

        // Default Values
        val recipeCount = 10
        val licensing = true
        val instructionsRequired = true
        val minCarbs = 0
        val minProtein = 0
        val minFat = 0
        val minCalories = 0

        var cuisinesString = ""
        var intolerancesString = ""

        for (cuisine in cuisines) {
            if (cuisine === cuisines.last()) {
                cuisinesString += cuisine
            }
            else {
                cuisinesString = "$cuisinesString$cuisine,+"
            }

        }

        for (intolerance in intolerances) {
            if (intolerance === intolerances.last()) {
                intolerancesString += intolerance
            }
            else {
                intolerancesString = "$intolerancesString$intolerance,+"
            }

        }

        var url = "https://api.spoonacular.com/recipes/complexSearch?" +
                "apiKey=$apiKey" +
                "&query=$recipeSearchText" +
                "&cuisine=$cuisinesString" +
                "&intolerances=$intolerancesString" +
                "&diet=$diets" +
                "&minCarbs=$minCarbs" +
                "&minProtein=$minProtein" +
                "&minFat=$minFat" +
                "&minCalories=$minCalories" +
                "&type=$mealTypes" +
                "&number=$recipeCount" +
                "&limitLicense=$licensing" +
                "&instructionRequired=$instructionsRequired"

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println("Json String: $body")

                val gson = GsonBuilder().create()

                search = gson.fromJson(body, TestSearch::class.java)
                recipeList = search.results
                println("Recipe List: $recipeList")
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute")
            }
        })

        return recipeList
    }

    // Takes in a recipe and returns all its details
    override fun getRecipeInfo(
        recipeId: Int
    ): RecipeInfo? {
        var moreInfo: RecipeInfo? = null

        var includeNutrition = false
        var url = "https://api.spoonacular.com/recipes/$recipeId/information?" +
                "apiKey=$apiKey" +
                "&includeNutrition=$includeNutrition"

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println("Json String: $body")

                val gson = GsonBuilder().create()

                moreInfo = gson.fromJson(body, RecipeInfo::class.java)
                println("Recipe List: $moreInfo")
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute")
            }
        })

        return moreInfo
    }

    // Takes in a recipe and returns its method.
    override fun getRecipeInstructions(
        recipeId: Int
    ): List<RecipeInstructions> {
        var recipeInstructions: List<RecipeInstructions> = emptyList()

        var url = "https://api.spoonacular.com/recipes/$recipeId/analyzedInstructions?" +
                "apiKey=$apiKey"

        // Get API response and store response as a JSON object.
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println("Json String: $body")

                val gson = GsonBuilder().create()

                recipeInstructions = gson.fromJson(body, Array<RecipeInstructions>::class.java).toList()
                println("Recipe List: $recipeInstructions")
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute")
            }
        })

        return recipeInstructions
    }





//
//    /** LESS IMPORTANT ONES TO-DO */
//
//    // Pass in list of ingredients along with other filters, returns list of recipes matching filter.
//    override fun getRecipeListByIngredientsFiltered(
//        ingredients: List<Ingredient>,
//        intolerances: List<Intolerance>,
//        diets: List<Diet>,
//        cuisines: List<Cuisine>,
//        mealTypes: List<MealType>
//    ): List<TestIngredientSearchRecipe> {
//
//        return emptyList()
//    }
//
//    //
//    override fun getAutocompletedIngredientList(
//        ingredientSearchText: String,
//        intolerances: List<Intolerance>
//    ): List<Ingredient> {
//
//        return emptyList()
//    }
//
//    override fun getAutocompletedRecipeList(
//        recipeSearchText: String
//    ): List<Recipe> {
//
//        return emptyList()
//    }
//
//    // Takes in a recipe, and returns similar ones.
//    override fun getSimilarRecipeList(
//        recipe: Recipe
//    ): List<Recipe> {
//
//        return emptyList()
//    }

}