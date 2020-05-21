package api

//Todo: Check that the mapping is correct according to the API
enum class Aisle {
    BAKING,
    HEALTH_FOODS,
    SPICES_AND_SEASONINGS,
    PASTA_AND_RICE,
    BAKERY,                 //maps to: "Bakery/Bread"
    REFRIGERATED,
    CANNED_AND_JARRED,
    FROZEN,
    BUTTERS_JAMS,           //maps to: "Nut butters, Jams, and Honey"
    OIL_VINEGAR,            //maps to: "Oil, Vinegar, Salad Dressing"
    CONDIMENTS,
    SAVORY_SNACKS,
    EGGS_DAIRY,             //maps to: "Milk, Eggs, Other Dairy"
    ETHNIC_FOODS,
    TEA_AND_COFFEE,
    MEAT,
    GOURMET,
    SWEET_SNACKS,
    GLUTEN_FREE,
    ALCOHOLIC_BEVERAGES,
    CEREAL,
    NUTS,
    BEVERAGES,
    PRODUCE,
    HOMEMADE,
    SEAFOOD,
    CHEESE,
    DRIED_FRUITS,
    ONLINE,
    GRILLING_SUPPLIES,
    BREAD
}

enum class Diet {
    GLUTEN_FREE,
    KETOGENIC,
    VEGETARIAN,
    LACTO_VEGETARIAN,
    OVO_VEGETARIAN,
    VEGAN,
    PESCETARIAN,
    PALEO,
    PRIMAL,
    WHOLE30
}

enum class Intolerance {
    DAIRY,
    EGG,
    GLUTEN,
    GRAIN,
    PEANUT,
    SEAFOOD,
    SESAME,
    SHELLFISH,
    SOY,
    SULFITE,
    TREE_NUT,
    WHEAT
}

enum class Cuisine {
    AFRICAN,
    AMERICAN,
    BRITISH,
    CAJUN,
    CARIBBEAN,
    CHINESE,
    EASTERN_EUROPEAN,
    EUROPEAN,
    FRENCH,
    GERMAN,
    GREEK,
    INDIAN,
    IRISH,
    ITALIAN,
    JAPANESE,
    JEWISH,
    KOREAN,
    LATIN_AMERICAN,
    MEDITERRANEAN,
    MEXICAN,
    MIDDLE_EASTERN,
    NORDIC,
    SOUTHERN,
    SPANISH,
    THAI,
    VIETAMESE
}

enum class MealType {
    MAIN_COURSE,
    SIDE_DISH,
    DESSERT,
    APPETIZER,
    SALAD,
    BREAD,
    BREAKFAST,
    SOUP,
    BEVERAGE,
    SAUCE,
    MARINADE,
    FINGERFOOD,
    SNACK,
    DRINK
}