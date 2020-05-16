package api

import android.graphics.Bitmap

/**
 * Template class to be used for various types of nutrition. E.g.:
 *  name = "Fat"
 *  value = "54"
 *  unit = "g"
 *
 *  toString() will return "54g".
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
                      val aisle: Aisle,
                      val image: Bitmap) {
    override fun equals(other: Any?): Boolean {
        return if (other is Ingredient) {
            this.id == other.id
        } else {
            super.equals(other)
        }
    }
}

/**
 * Recipe class which maps to how recipes are returned when simply searching for recipes.
 */
data class Recipe(val id: Int,
                  val name: String,
                  val nutritionList: List<Nutrition>,
                  val image: Bitmap)

/**
 * Recipe class which includes extra information returned when searching by ingredient.
 */
data class IngredientSearchRecipe(val recipe: Recipe,
                                  val missedIngredients: List<Ingredient>,
                                  val usedIngredients: List<Ingredient>,
                                  val unusedIngredients: List<Ingredient>)

/**
 * Recipe class which includes the steps involved in actually creating the dish.
 */
data class RecipeMethod(val recipe: Recipe,
                        val stepList: List<String>)