package api

/**
 * List of pairs
 */
val aisleMapToString = listOf(
    Pair(Aisle.BAKING, "Baking"),
    Pair(Aisle.HEALTH_FOODS, "Health"),
    Pair(Aisle.SPICES_AND_SEASONINGS, "Spices"),
    Pair(Aisle.PASTA_AND_RICE, "Pasta & Rice"),
    Pair(Aisle.BAKERY, "Bakery"),
    Pair(Aisle.REFRIGERATED, "Refrigerated"),
    Pair(Aisle.CANNED_AND_JARRED, "Cans & Jars"),
    Pair(Aisle.FROZEN, "Frozen"),
    Pair(Aisle.BUTTERS_JAMS, "Butters & Jams"),
    Pair(Aisle.OIL_VINEGAR, "Oils, vinegars etc."),
    Pair(Aisle.CONDIMENTS, "Condiments"),
    Pair(Aisle.SAVORY_SNACKS, "Savory snacks"),
    Pair(Aisle.EGGS_DAIRY, "Eggs & Dairy"),
    Pair(Aisle.ETHNIC_FOODS, "Ethnic"),
    Pair(Aisle.TEA_AND_COFFEE, "Tea & Coffee"),
    Pair(Aisle.MEAT, "Meat"),
    Pair(Aisle.GOURMET, "Gourmet"),
    Pair(Aisle.SWEET_SNACKS, "Sweet snacks"),
    Pair(Aisle.GLUTEN_FREE, "Gluten-free"),
    Pair(Aisle.ALCOHOLIC_BEVERAGES, "Alcoholic"),
    Pair(Aisle.CEREAL, "Cereal"),
    Pair(Aisle.NUTS, "Nuts"),
    Pair(Aisle.BEVERAGES, "Beverages"),
    Pair(Aisle.PRODUCE, "Produce"),
    Pair(Aisle.HOMEMADE, "Homemade"),
    Pair(Aisle.SEAFOOD, "Seafood"),
    Pair(Aisle.CHEESE, "Cheese"),
    Pair(Aisle.DRIED_FRUITS, "Dried fruit"),
    Pair(Aisle.ONLINE, "Online"), //TODO: What actually is this one?
    Pair(Aisle.GRILLING_SUPPLIES, "Grilling"),
    Pair(Aisle.BREAD, "Bread")
)

/**
 * Helper function to get the AisleString from the Aisle enum.
 */
fun getAisleStringFromAisle(aisle: Aisle): String? {
    return aisleMapToString.first { it.first == aisle }.second
}

/**
 * Helper function to get the Aisle enum from the AisleString.
 */
fun getAisleFromAisleString(aisleString: String): Aisle {
    return aisleMapToString.first { it.second == aisleString }.first
}