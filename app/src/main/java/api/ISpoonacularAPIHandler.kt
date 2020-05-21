package api

interface ISpoonacularAPIHandler {

    /**
     * (Docs: https://spoonacular.com/food-api/docs#Search-Recipes-by-Ingredients)
     * Will take a list of ingredients and return a list of IngredientSearchRecipes which
     *  are recipes that are relevant to those ingredients.
     */
    fun getRecipeListByIngredients(
        ingredients: List<TestIngredient>
    ): List<TestIngredientSearchRecipe>

    /**
     * (Docs: https://spoonacular.com/food-api/docs#Search-Recipes-Complex)
     * Returns a list of recipes based upon the user's search text and their saved preferences
     *  and intolerances.
     */
    fun getRecipeListBySearch(
        recipeSearchText: String,
        intolerances: List<String>,
        diets: String,
        cuisines: List<String>,
        mealTypes: String
    ): List<TestSearchRecipe>

     fun getRecipeInfo(
        recipeId: Int
     ): RecipeInfo?

    /**
     * (Docs: https://spoonacular.com/food-api/docs#Get-Analyzed-Recipe-Instructions)
     * Will take a recipe and get the steps in the method of the recipe.
     */
    fun getRecipeInstructions(
        recipeId: Int
    ): List<RecipeInstructions>

//
//    /** LESS IMPORTANT ONES TO-DO */
//
//    /**
//     * (Docs: https://spoonacular.com/food-api/docs#Search-Recipes-by-Ingredients)
//     * Extended version of 'getRecipeListByIngredients'; since Spoonacular doesn't have
//     *  a function to filter by intolerances, cuisine, diet, equipment etc., we may have to
//     *  manually filter the recipes returned from 'getRecipeListByIngredients' ourselves.
//     *
//     *  Idea (to save query quota as much as possible): Query by ingredients to get the list
//     *   of recipes, then run a 'Get recipe information bulk query'
//     *      (Docs: https://spoonacular.com/food-api/docs#Get-Recipe-Information-Bulk)
//     *   on all recipes returned and filter the original list of recipes returned by
//     *   'getRecipeListByIngredients' based upon the intolerances, diet etc. passed in.
//     */
//    fun getRecipeListByIngredientsFiltered(
//        ingredients: List<Ingredient>,
//        intolerances: List<Intolerance>,
//        diets: List<Diet>,
//        cuisines: List<Cuisine>,
//        mealTypes: List<MealType>
//    ): List<TestIngredientSearchRecipe>
//
//    /**
//     * (Docs: https://spoonacular.com/food-api/docs#Autocomplete-Ingredient-Search)
//     * Will take the string text the user has typed in order to find an ingredient and
//     *  return a list, of sensible length (e.g. 30), of ingredients which will be shown in a
//     *  grid for the user to pick from.
//     *      E.g. user text = "App"
//     *          returns = List("Apple", "Apple sauce", ...)
//     */
//    fun getAutocompletedIngredientList(
//        ingredientSearchText: String,
//        intolerances: List<Intolerance>
//    ): List<Ingredient>
//
//    /**
//     * (Docs: https://spoonacular.com/food-api/docs#Autocomplete-Recipe-Search)
//     * Will take the string text the user has typed in order to find a recipe and
//     *  return a list, of sensible length (e.g. 6), of recipes which act as an autocomplete.
//     *      E.g. user text = "chick"
//     *          returns = List("Chicken", "Chicken BBQ", "Chicken BLT", "Chicken Pie", ...)
//     */
//
//    fun getAutocompletedRecipeList(
//        recipeSearchText: String
//    ): List<Recipe>
//
//    /**
//     * (Docs: https://spoonacular.com/food-api/docs#Get-Similar-Recipes)
//     * Will take a recipe and return a list, of length approx 50-100, of recipes which are
//     *  similar to it.
//     */
//    fun getSimilarRecipeList(
//        recipe: Recipe
//    ): List<Recipe>
//
}