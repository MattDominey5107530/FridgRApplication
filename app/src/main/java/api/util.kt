package api

/**
 * List of pairs for aisles
 */
val aisleMapToString = listOf(
    Pair(Aisle.BAKING, "Baking"),
    Pair(Aisle.HEALTH_FOODS, "Health Foods"),
    Pair(Aisle.SPICES_AND_SEASONINGS, "Spices and Seasonings"),
    Pair(Aisle.PASTA_AND_RICE, "Pasta & Rice"),
    Pair(Aisle.BAKERY, "Bakery/Bread"),
    Pair(Aisle.REFRIGERATED, "Refrigerated"),
    Pair(Aisle.CANNED_AND_JARRED, "Canned and Jarred"),
    Pair(Aisle.FROZEN, "Frozen"),
    Pair(Aisle.BUTTERS_JAMS, "Nut butters, Jams, and Honey"),
    Pair(Aisle.OIL_VINEGAR, "Oil, Vinegar, Salad Dressing"),
    Pair(Aisle.CONDIMENTS, "Condiments"),
    Pair(Aisle.SAVORY_SNACKS, "Savory Snacks"),
    Pair(Aisle.EGGS_DAIRY, "Milk, Eggs, Other Dairy"),
    Pair(Aisle.ETHNIC_FOODS, "Ethnic Foods"),
    Pair(Aisle.TEA_AND_COFFEE, "Tea and Coffee"),
    Pair(Aisle.MEAT, "Meat"),
    Pair(Aisle.GOURMET, "Gourmet"),
    Pair(Aisle.SWEET_SNACKS, "Sweet Snacks"),
    Pair(Aisle.GLUTEN_FREE, "Gluten Free"),
    Pair(Aisle.ALCOHOLIC_BEVERAGES, "Alcoholic Beverages"),
    Pair(Aisle.CEREAL, "Cereal"),
    Pair(Aisle.NUTS, "Nuts"),
    Pair(Aisle.BEVERAGES, "Beverages"),
    Pair(Aisle.PRODUCE, "Produce"),
    Pair(Aisle.HOMEMADE, "Not in Grocery Store/Homemade"),
    Pair(Aisle.SEAFOOD, "Seafood"),
    Pair(Aisle.CHEESE, "Cheese"),
    Pair(Aisle.DRIED_FRUITS, "Dried Fruits"),
    Pair(Aisle.ONLINE, "Online"),
    Pair(Aisle.GRILLING_SUPPLIES, "Grilling Supplies"),
    Pair(Aisle.BREAD, "Bread")
)

/**
 * Helper function to get the AisleString from the Aisle enum.
 */
fun getAisleStringFromAisle(aisle: Aisle): String {
    return aisleMapToString.first { it.first == aisle }.second
}

/**
 * Helper function to get the Aisle enum from the AisleString.
 */
fun getAisleFromAisleString(aisleString: String): Aisle {
    return try {
        aisleMapToString.first { it.second == aisleString }.first
    } catch (e: Exception) {
        println("$aisleString is not an acceptable aisle.")
        e.printStackTrace()
        Aisle.HEALTH_FOODS
    }
}

/**
 * List of pairs for intolerance
 */
val intoleranceMapToString = listOf(
    Pair(Intolerance.DAIRY, "Dairy"),
    Pair(Intolerance.EGG, "Egg"),
    Pair(Intolerance.GLUTEN, "Gluten"),
    Pair(Intolerance.GRAIN, "Grain"),
    Pair(Intolerance.PEANUT, "Peanut"),
    Pair(Intolerance.SEAFOOD, "Seafood"),
    Pair(Intolerance.SESAME, "Sesame"),
    Pair(Intolerance.SHELLFISH, "Shellfish"),
    Pair(Intolerance.SOY, "Soy"),
    Pair(Intolerance.SULFITE, "Sulfite"),
    Pair(Intolerance.TREE_NUT, "Tree Nut"),
    Pair(Intolerance.WHEAT, "Wheat")
)

/**
 * Helper function to get the IntoleranceString from the Intolerance enum.
 */
fun getIntoleranceStringFromIntolerance(intolerance: Intolerance): String? {
    return intoleranceMapToString.first { it.first == intolerance }.second
}

/**
 * Helper function to get the Intolerance enum from the IntoleranceString.
 */
fun getIntoleranceFromIntoleranceString(intoleranceString: String): Intolerance? {
    return try {
        intoleranceMapToString.first { it.second == intoleranceString }.first
    } catch (e: Exception) {
        null
    }
}

/**
 * List of pairs for diets
 */
val dietMapToString = listOf(
    Pair(Diet.GLUTEN_FREE, "Gluten Free"),
    Pair(Diet.KETOGENIC, "Ketogenic"),
    Pair(Diet.VEGETARIAN, "Vegetarian"),
    Pair(Diet.LACTO_VEGETARIAN, "Lacto-Vegetarian"),
    Pair(Diet.OVO_VEGETARIAN, "Ovo-Vegetarian"),
    Pair(Diet.VEGAN, "Vegan"),
    Pair(Diet.PESCETARIAN, "Pescetarian"),
    Pair(Diet.PALEO, "Paleo"),
    Pair(Diet.PRIMAL, "Primal"),
    Pair(Diet.WHOLE30, "Whole30")
)

/**
 * Helper function to get the DietString from the Diet enum.
 */
fun getDietStringFromDiet(diet: Diet): String? {
    return dietMapToString.first { it.first == diet }.second
}

/**
 * Helper function to get the Diet enum from the DietString.
 */
fun getDietFromDietString(dietString: String): Diet? {
    return try {
        dietMapToString.first { it.second == dietString }.first
    } catch (e: Exception) {
        null
    }
}

/**
 * List of pairs for cuisines
 */
val cuisineMapToString = listOf(
    Pair(Cuisine.AFRICAN, "African"),
    Pair(Cuisine.AMERICAN, "American"),
    Pair(Cuisine.BRITISH, "British"),
    Pair(Cuisine.CAJUN, "Cajun"),
    Pair(Cuisine.CARIBBEAN, "Caribbean"),
    Pair(Cuisine.CHINESE, "Chinese"),
    Pair(Cuisine.EASTERN_EUROPEAN, "Eastern European"),
    Pair(Cuisine.EUROPEAN, "European"),
    Pair(Cuisine.FRENCH, "French"),
    Pair(Cuisine.GERMAN, "German"),
    Pair(Cuisine.GREEK, "Greek"),
    Pair(Cuisine.INDIAN, "Indian"),
    Pair(Cuisine.IRISH, "Irish"),
    Pair(Cuisine.ITALIAN, "Italian"),
    Pair(Cuisine.JAPANESE, "Japanese"),
    Pair(Cuisine.JEWISH, "Jewish"),
    Pair(Cuisine.KOREAN, "Korean"),
    Pair(Cuisine.LATIN_AMERICAN, "Latin American"),
    Pair(Cuisine.MEDITERRANEAN, "Mediterranean"),
    Pair(Cuisine.MEXICAN, "Mexican"),
    Pair(Cuisine.MIDDLE_EASTERN, "Middle Eastern"),
    Pair(Cuisine.NORDIC, "Nordic"),
    Pair(Cuisine.SOUTHERN, "Southern"),
    Pair(Cuisine.SPANISH, "Spanish"),
    Pair(Cuisine.THAI, "Thai"),
    Pair(Cuisine.VIETNAMESE, "Vietnamese")
)

/**
 * Helper function to get the DietString from the Diet enum.
 */
fun getCuisineStringFromCuisine(cuisine: Cuisine): String? {
    return cuisineMapToString.first { it.first == cuisine }.second
}

/**
 * Helper function to get the Diet enum from the DietString.
 */
fun getCuisineFromCuisineString(cuisineString: String): Cuisine? {
    return try {
        cuisineMapToString.first { it.second == cuisineString }.first
    } catch (e: Exception) {
        null
    }
}

/**
 * List of pairs for meal types
 */
val mealtypeMapToString = listOf(
    Pair(MealType.MAIN_COURSE, "main_course"),
    Pair(MealType.SIDE_DISH, "side_dish"),
    Pair(MealType.DESSERT, "dessert"),
    Pair(MealType.APPETIZER, "appetizer"),
    Pair(MealType.SALAD, "salad"),
    Pair(MealType.BREAD, "bread"),
    Pair(MealType.BREAKFAST, "breakfast"),
    Pair(MealType.SOUP, "soup"),
    Pair(MealType.BEVERAGE, "beverage"),
    Pair(MealType.SAUCE, "sauce"),
    Pair(MealType.MARINADE, "marinade"),
    Pair(MealType.FINGERFOOD, "fingerfood"),
    Pair(MealType.SNACK, "snack"),
    Pair(MealType.DRINK, "drink")
)

/**
 * Helper function to get the MealTypeString from the MealType enum.
 */
fun getMealTypeStringFromMealType(mealType: MealType): String? {
    return mealtypeMapToString.first { it.first == mealType }.second
}

/**
 * Helper function to get the MealType enum from the MealTypeString.
 */
fun getMealTypeFromMealTypeString(mealTypeString: String): MealType? {
    return try {
        mealtypeMapToString.first { it.second == mealTypeString }.first
    } catch (e: Exception) {
        null
    }
}

val ingredientImageStringPrefix = "https://spoonacular.com/cdn/ingredients_100x100/"

/**
 * Conversion functions for the data classes
 */
fun TestIngredient.toIngredient(): Ingredient {
    val aisleList: List<Aisle> = this.aisle.split(";").map { getAisleFromAisleString(it) }
    //Add the prefix 100x100 URL to the image URL
    val imageString =
        (if (this.image.startsWith(ingredientImageStringPrefix)) "" else ingredientImageStringPrefix) + this.image

    return Ingredient(
        this.id,
        this.name,
        aisleList,
        imageString
    )
}

fun TestIngredientSearchRecipe.toIngredientSearchRecipe(): IngredientSearchRecipe {
    return IngredientSearchRecipe(
        Recipe(
            this.id,
            this.title,
            emptyList(), //No nutrition list returned by this class
            this.image
        ),
        this.missedIngredients.map { it.toIngredient() },
        this.usedIngredients.map { it.toIngredient() },
        this.unusedIngredients.map { it.toIngredient() }
    )
}

fun TestSearchRecipe.toRecipe(): Recipe {
    return Recipe(
        this.id,
        this.title,
        this.nutrition.map { it.toNutrition() },
        this.image
    )
}

fun TestNutrition.toNutrition(): Nutrition {
    return Nutrition(
        this.title,
        this.amount.toDouble(),
        this.unit
    )
}