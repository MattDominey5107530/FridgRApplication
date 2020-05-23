package api

import com.example.fridgr.RecipeSearchFragment

interface ISpoonacularAPIHandler {

    /**
     * (Docs: https://spoonacular.com/food-api/docs#Search-Recipes-by-Ingredients)
     * Will take a list of ingredients and return a list of IngredientSearchRecipes which
     *  are recipes that are relevant to those ingredients.
     */
    suspend fun getRecipeListByIngredients(
        ingredients: List<Ingredient>
    ): List<IngredientSearchRecipe>

    /**
     * (Docs: https://spoonacular.com/food-api/docs#Search-Recipes-Complex)
     * Returns a list of recipes based upon the user's search text and their saved preferences
     *  and intolerances.
     */
    suspend fun getRecipeListBySearch(
        recipeSearchText: String,
        intolerances: List<Intolerance>,
        diet: Diet?,
        cuisines: List<Cuisine>,
        mealType: MealType?,
        nutritionFilters: RecipeSearchFragment.NutritionFilters?
    ): List<Recipe>

    suspend fun getRecipeInfo(
        recipeId: Int
    ): RecipeInfo?

    /**
     * (Docs: https://spoonacular.com/food-api/docs#Get-Analyzed-Recipe-Instructions)
     * Will take a recipe and get the steps in the method of the recipe.
     */
    suspend fun getRecipeInstructions(
        recipeId: Int
    ): List<RecipeInstructions>

    /** (Docs: https://spoonacular.com/food-api/docs#Autocomplete-Ingredient-Search)
     * Will take the string text the user has typed in order to find an ingredient and
     *  return a list, of sensible length (e.g. 30), of ingredients which will be shown in a
     *  grid for the user to pick from.
     *      E.g. user text = "App"
     *          returns = List("Apple", "Apple sauce", ...)
     */
    suspend fun getAutocompletedIngredientList(
        ingredientSearchText: String,
        intolerances: List<Intolerance>
    ): List<Ingredient>

    /**
     * (Docs: https://spoonacular.com/food-api/docs#Autocomplete-Recipe-Search)
     * Will take the string text the user has typed in order to find a recipe and
     *  return a list, of sensible length (e.g. 6), of recipes which act as an autocomplete.
     *      E.g. user text = "chick"
     *          returns = List("Chicken", "Chicken BBQ", "Chicken BLT", "Chicken Pie", ...)
     */

    suspend fun getAutocompletedRecipeList(
        recipeSearchText: String
    ): List<TestSearchRecipe>


    /**
     * (Docs: https://spoonacular.com/food-api/docs#Get-Similar-Recipes)
     * Will take a recipe and return a list, of length approx 50-100, of recipes which are
     *  similar to it.
     */
    suspend fun getSimilarRecipeList(
        id: Int
    ): List<Recipe>

}