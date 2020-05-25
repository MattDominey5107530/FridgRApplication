package user_database

import android.content.Context
import com.example.fridgr.local_storage.getUserToken

/**
 * Extension functions to comply with GDPR data erasure.
 * ***UPON FEEDBACK FROM FORENSIC***
 */

/**
 * Extension function to get all the user's api queries for a particular user_id
 */
private fun UserDatabaseHandler.getUserQueries(userId: Int): List<String> {
    this.connect()
    val query =
        "SELECT query_text FROM UserSearchQueries WHERE user_id=$userId;"
    val resultSet = this.getResultSet(query)

    val userQueries = arrayListOf<String>()

    if (resultSet != null) {
        while (resultSet.next()) {
            userQueries.add(resultSet.getString("query_text"))
        }
    }

    return userQueries.toList()
}

/**
 * Extension function to write a user's query string to the database.
 */
fun UserDatabaseHandler.writeCurrentUserQuery(context: Context, queryString: String) {
    this.connect()
    val userToken = getUserToken(context)

    if (userToken != null) {
        var query =
            "SELECT user_id FROM Users WHERE user_token='$userToken';"
        val resultSet = getResultSet(query)

        if (resultSet != null && resultSet.next()) {
            val userId = resultSet.getInt("user_id")

            query =
                "INSERT INTO UserSearchQueries VALUES($userId, '$queryString');"
            updateQuery(query)
        }
    }
}


/**
 * Extension function which fetches all data stored on the user (i.e. every row in the database
 *  with user_id = X).
 *  In the format:
 *      Username: <>
 *      Password: <OMITTED>
 *      Nickname: <>
 *
 *      Diet: <None/<>>
 *      Intolerances: <>, <>, <>
 *      Favourite cuisines: <>, <>, <>
 *
 *      All search queries:
 *          <>
 *          <>
 *          <>
 */
fun UserDatabaseHandler.requestAllUserData(userToken: String): String? {
    this.connect()
    var userDataString = ""

    var query =
        "SELECT user_id FROM Users WHERE user_token='$userToken';"
    var resultSet = getResultSet(query)

    if (resultSet != null && resultSet.next()) {
        val userId = resultSet.getInt("user_id")

        //Get Users information
        query =
            "SELECT username, nickname FROM Users WHERE user_id=$userId;"
        resultSet = getResultSet(query)
        if (resultSet != null && resultSet.next()) {
            userDataString += "Username: ${resultSet.getString("username")}\n" +
                    "Password: OMITTED\n" +
                    "Nickname: ${resultSet.getString("nickname")}\n\n"
        } else {
            return null
        }

        //Get user preferences (diet and intolerances)
        val userPreferences = getUserPreferences(userToken)
        if (userPreferences != null) {
            userDataString += "Diet: ${userPreferences.diet?.name ?: "None"}\n" +
                    "Intolerances: ${userPreferences.intolerances.joinToString(", ") { it.name }}\n"
        } else {
            return null
        }

        //Get user cuisines
        val userCuisines = getUserCuisines(userToken)
        userDataString += "Favourite cuisines: ${userCuisines.joinToString(", ") { it.name }}\n\n"

        //Get query strings
        val queryStrings = getUserQueries(userId)
        userDataString += "All search queries: \n" +
                queryStrings

        return userDataString
    } else {
        return null
    }
}

/**
 * Extension function which deletes a user's account and all data stored on them from the user
 *  database.
 */
fun UserDatabaseHandler.deleteAllUserData(userToken: String) {
    this.connect()
    val query =
        "SELECT user_id FROM Users WHERE user_token='$userToken';"
    val resultSet = getResultSet(query)

    if (resultSet != null && resultSet.next()) {
        val userId = resultSet.getInt("user_id")
        val tableNamesToDeleteFrom = listOf(
            "diets",
            "Cuisines",
            "Intolerances",
            "UserSearchQueries",
            "Users"
        )

        for (tableName in tableNamesToDeleteFrom) {
            val deleteQuery =
                "DELETE FROM $tableName WHERE user_id=$userId;"
            updateQuery(deleteQuery)
        }
    }
}

/**
 * Extension function which registers a user to the user database
 */
fun UserDatabaseHandler.register(username: String, password: String): Boolean {
    this.connect()
    val passwordHash = hashPassword(password)
    var query =
        "INSERT INTO Users (username, password, nickname) VALUES ('$username', '$passwordHash', '$username');"
    updateQuery(query)

    val userToken = authenticate(username, password)

    query =
        "SELECT user_id FROM Users WHERE user_token='$userToken';"
    val resultSet = getResultSet(query)
    if (resultSet != null && resultSet.next()) {
        val userId = resultSet.getInt("user_id")
        val queryList = listOf(
            "INSERT INTO Cuisines VALUES ($userId, false, false, false, false, false, false, " +
                    "false, false, false, false, false, false, false, false, false, " +
                    "false, false, false, false, false, false, false, false, false);",
            "INSERT INTO diets VALUES ($userId, false, false, false, false, false, false, " +
                    "false, false, false, false);",
            "INSERT INTO Intolerances VALUES ($userId, false, false, false, false, false, " +
                    "false, false, false, false, false, false, false);"
        )
        for (insertQuery in queryList) {
            updateQuery(insertQuery)
        }
        this.conn?.close()
        return true
    } else {
        this.conn?.close()
        return false
    }

}
