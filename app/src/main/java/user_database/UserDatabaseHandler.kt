package user_database

import api.Cuisine
import api.Diet
import api.Intolerance
import com.example.fridgr.local_storage.UserPreferences
import java.security.MessageDigest
import java.sql.*
import java.util.*

object UserDatabaseHandler : IUserDatabaseHandler {
    private var conn: Connection? = null
    private const val username: String = "app_user"
    private const val password: String = "KJDjfkJKDL29Km8FK,"
    private const val host: String = "fridgrinstance.c3qjm5y8runj.eu-west-2.rds.amazonaws.com"
    private const val port: String = "3306"

    /**
     * Helper function to establish a connection to the database.
     */
    fun connect() {
        val connectionProps = Properties()
        connectionProps["user"] = username
        connectionProps["password"] = password

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection(
                "jdbc:mysql://$host:$port/fridgrUserDatabase",
                connectionProps
            )
        } catch (sqlE: SQLException) {
            //SQL exceptions
            sqlE.printStackTrace()
        } catch (e: Exception) {
            //Any other exceptions, (possible phone not connected to the internet)
            e.printStackTrace()
        }
    }

    /**
     * Helper function to use with SELECT and any GET queries.
     */
    fun getResultSet(query: String): ResultSet? {
        requireNotNull(conn)

        val stmt: Statement?

        return try {
            stmt = conn!!.prepareStatement(query)
            stmt.executeQuery()
        } catch (sqlE: SQLException) {
            //SQL exceptions
            sqlE.printStackTrace()
            null
        }
    }

    /**
     * Helper function to use with UPDATE/INSERT and any POST queries.
     */
    fun updateQuery(query: String) {
        requireNotNull(conn)

        val stmt: Statement?

        try {
            stmt = conn!!.prepareStatement(query)
            stmt.executeUpdate()
        } catch (sqlE: SQLException) {
            sqlE.printStackTrace()
        }
    }

    /**
     * Function to hash & salt the password before passing it to the database.
     */
    private fun hashPassword(password: String): String {
        val algorithm = "SHA-256"
        return MessageDigest.getInstance(algorithm).digest(password.toByteArray()).fold("",
            { str, it -> str + "%02x".format(it) })
    }

    /**
     * Function to generate a random UUID string to use as the user token.
     */
    private fun getRandomUserToken(): String {
        return java.util.UUID.randomUUID().toString()
    }

    /**
     * Function to authenticate the user; returns the random user token if successful (which will
     *  be used to authenticate the user in later queries) and null if not.
     */
    override fun authenticate(username: String, password: String): String? {
        this.connect()
        val userToken: String = getRandomUserToken()
        val passwordHash: String = hashPassword(password)

        var query =
            "SELECT user_id FROM Users WHERE username='$username' AND password='$passwordHash';"
        val resultSet = getResultSet(query)

        if (resultSet != null && resultSet.next()) {
            val userId = resultSet.getString("user_id")
            query =
                "UPDATE Users SET user_token = '$userToken' WHERE user_id=$userId;"
            updateQuery(query)
            return userToken
        }

        return null
    }

    override fun getUserPreferences(user_token: String): UserPreferences? {
        this.connect()

        val intolerances: MutableList<Intolerance> = emptyList<Intolerance>().toMutableList()
        var diet: Diet? = null

        //Select user based on stored user_token
        var query =
            "SELECT user_id FROM Users WHERE user_token='$user_token';"
        var resultSet = getResultSet(query)

        //select all data from diets and intolerances stored on user
        if (resultSet != null && resultSet.next()) {
            val userId = resultSet.getInt("user_id")
            query =
                "SELECT * FROM diets WHERE user_id=$userId;"
            resultSet = getResultSet(query)
            if (resultSet != null && resultSet.next()) {
                for (i in 0 until 10) {
                    if (resultSet.getBoolean(i + 2)) {
                        diet = Diet.values()[i]
                    }
                }
            }
            query =
                "SELECT * FROM Intolerances WHERE user_id=$userId;"
            resultSet = getResultSet(query)
            if (resultSet != null && resultSet.next()) {
                for (i in 0 until 12) {
                    if (resultSet.getBoolean(i + 2)) {
                        intolerances.add(Intolerance.values()[i])
                    }
                }
            }
        }

        return UserPreferences(intolerances, diet)
    }

    override fun writeUserPreferences(user_token: String, userPreferences: UserPreferences) {
        this.connect()
        val intolerances: List<Intolerance> = userPreferences.intolerances
        val diet = userPreferences.diet?.name

        //Select user based on stored user_token
        var query =
            "SELECT user_id FROM Users WHERE user_token='$user_token';"
        val resultSet = getResultSet(query)

        if (resultSet != null && resultSet.next()) {
                val userId = resultSet.getString("user_id")

            //update diet table
            if (diet != null) {

                query =
                    "UPDATE diets SET gluten_free = false, ketogenic = false, vegetarian = false," +
                            "lacto_vegetarian = false, ovo_vegetarian = false, vegan = false," +
                            "pescetarian = false, paleo = false, primal = false, whole30 = false WHERE user_id=$userId;"
                updateQuery(query)

                query =
                    "UPDATE diets SET $diet = true WHERE user_id=$userId;"
                updateQuery(query)
            }

            //update intolerances table
            if (intolerances.isNotEmpty()) {

                query =
                    "UPDATE Intolerances SET dairy = false, egg = false, gluten = false, grain = false," +
                            "peanut = false, seafood = false, sesame = false, shellfish = false, soy = false," +
                            "sulfite = false, tree_nut = false, wheat = false WHERE user_id=$userId;"
                updateQuery(query)


                for (intolerance in intolerances) {
                    query =
                        "UPDATE Intolerances SET $intolerance = true WHERE user_id=$userId;"
                    updateQuery(query)
                }

            }

        }

    }

    override fun getUserCuisines(user_token: String): List<Cuisine> {
        this.connect()

        val cuisine: MutableList<Cuisine> = emptyList<Cuisine>().toMutableList()

        //Select user based on stored user_token
        var query =
            "SELECT user_id FROM Users WHERE user_token='$user_token';"
        var resultSet = getResultSet(query)

        //select all data from Cuisines stored on user
        if (resultSet != null && resultSet.next()) {
            val userId = resultSet.getInt("user_id")
            query =
                "SELECT * FROM cuisines WHERE user_id=$userId;"
            resultSet = getResultSet(query)
            if (resultSet != null && resultSet.next()) {
                for (i in 0 until 23) {
                    if (resultSet.getBoolean(i + 2)) {
                        cuisine.add(Cuisine.values()[i])
                    }
                }
            }
        }
        return cuisine
    }

    override fun writeUserCuisines(user_token: String, userCuisines: List<Cuisine>) {
        connect()

        //Select user based on stored user_token
        var query =
            "SELECT user_id FROM Users WHERE user_token='$user_token';"
        val resultSet = getResultSet(query)

        if (resultSet != null && resultSet.next()) {
            val userId = resultSet.getString("user_id")

            //update cuisines table
            if (userCuisines.isNotEmpty()) {
                query =
                    "UPDATE cuisines SET african = false, american=false, british=false, cajun=false," +
                            "caribbean = false, chinese = false, eastern_european = false, european = false," +
                            "french = false, german = false, greek = false, indian = false, irish = false," +
                            "italian = false, japanese = false, jewish = false, korean = false," +
                            "latin_american = false, middle_eastern = false, nordic = false, southern = false," +
                            "spanish = false, thai = false, vietamese = false;"
                updateQuery(query)

                for (cuisine in userCuisines) {
                    query =
                        "UPDATE cuisines SET $cuisine = true WHERE user_id=$userId;"
                    updateQuery(query)
                }
            }
        }
    }

    override fun getUserNickname(userToken: String): String {
        this.connect()

        val userNickname: String

        //Select user based on stored user_token
        val query =
            "SELECT nickname FROM Users WHERE user_token='$userToken';"
        val resultSet = getResultSet(query)

        //select all data from Cuisines stored on user
        if (resultSet != null && resultSet.next()) {
            userNickname = resultSet.getString("nickname")
        } else {
            userNickname = "INVALID"
        }

        return userNickname
    }

    override fun writeUserNickname(userToken: String, newNickname: String) {
        this.connect()

        //Select user based on stored user_token
        var query =
            "SELECT user_id FROM Users WHERE user_token='$userToken';"
        val resultSet = getResultSet(query)

        if (resultSet != null && resultSet.next()) {
            val userId = resultSet.getString("user_id")

            query =
                "UPDATE Users SET nickname='$newNickname' WHERE user_id=$userId;"
            updateQuery(query)
        }
    }

    /**
     * Function which returns whether a string is valid to be passed into the database
     * ***UPON FEEDBACK FROM FORENSIC***
     */
    fun isTextLegal(): Boolean {
        //TODO: if it contains anything dodgy then return false
        return true
    }

}