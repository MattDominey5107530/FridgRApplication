package api

import android.graphics.Bitmap

/**
 * Template class to be used for various types of nutrition. E.g.:
 *  name = "Fat"
 *  value = "54"
 *  unit = "g"
 *
 *  toString() will return "Fat: 54g".
 */
data class Nutrition(val name: String,
                     val value: Double,
                     val unit: String) {
    override fun toString(): String = "$name: $value $unit"
}

/**
 * Ingredient class which maps to how ingredients are returned by the API.
 */
data class Ingredient(val id: Int,
                      val name: String,
                      val aisle: List<Aisle>,
                      val imageString: String) {
    override fun equals(other: Any?): Boolean {
        return if (other is Ingredient) {
            this.id == other.id
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + aisle.hashCode()
        result = 31 * result + imageString.hashCode()
        return result
    }
}

/**
 * Recipe class which maps to how recipes are returned when simply searching for recipes.
 */
data class Recipe(val id: Int,
                  val name: String,
                  val nutritionList: List<Nutrition>,
                  val imageString: String)

/**
 * Recipe class which includes extra information returned when searching by ingredient.
 */
data class IngredientSearchRecipe(val recipe: Recipe,
                                  val missedIngredients: List<Ingredient>,
                                  val usedIngredients: List<Ingredient>,
                                  val unusedIngredients: List<Ingredient>)

/**
 *  New Data Classes
 */

/** Simple Recipe Data From Ingredient Search */

data class TestIngredientSearchRecipe(
    val id: Int,
    val image: String,
    val missedIngredients: List<TestIngredient>,
    val title: String,
    val usedIngredients: List<TestIngredient>,
    val unusedIngredients: List<TestIngredient>
)

data class TestIngredient(
    val aisle: String,
    val amount: Float,
    val id: Int,
    val image: String,
    val name: String,
    val unit: String
)

/** Simple Recipe Data From Text Search */

data class TestSearch(
    val offset: Int,
    val number: Int,
    val results: List<TestSearchRecipe>,
    val totalResults: Int
)


data class TestSearchRecipe(
    val id: Int,
    val title: String,
    val image: String,
    val nutrition: List<TestNutrition>
)

data class TestNutrition(
    val title: String,
    val amount: Float,
    val unit: String
) {
    override fun toString(): String = amount.toString() + unit
}

/** Detailed information on any given recipe */

data class RecipeInfo(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val readyInMinutes: Int,
    val sourceUrl: String,
    val extendedIngredients: List<ExtendedIngredients>,
    val summary: String
)

data class ExtendedIngredients(
    val aisle: String,
    val amount: Float,
    val id: Int,
    val image: String,
    val measures: Measures,
    val name: String,
    val unit: String
)

data class Measures(
    val metric: MeasureValues,
    val us: MeasureValues
)

data class MeasureValues(
    val amount: Float,
    val unitLong: String,
    val unitShort: String
)

/** Detailed instructions on any given recipe */

data class RecipeInstructions(
    val name: String,
    val steps: List<RecipeSteps>
)

data class RecipeSteps(
    val equipment: List<Equipment>,
    val ingredients: List<RecipeStepsIngredient>,
    val number: Int,
    val step: String
)

data class RecipeStepsIngredient(
    val id: Int,
    val name: String,
    val image: String
)

data class Equipment(
    val id: Int,
    val image: String,
    val name: String,
    val temperature: Temperature
)

data class Temperature(
    val number: Float,
    val unit: String
)
