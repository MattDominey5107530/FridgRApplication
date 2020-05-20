package user_database

import android.util.Log
import api.Cuisine
import com.example.fridgr.local_storage.UserPreferences
import java.sql.*
import java.util.*

const val DATABASE_USERNAME = "app_user"
const val DATABASE_PASSWORD = "KJDjfkJKDL29Km8FK,"

object UserDatabaseHandler : IUserDatabaseHandler {
    internal var conn: Connection? = null
    internal val username: String = DATABASE_USERNAME
    internal val password: String = DATABASE_PASSWORD
    internal val host: String = "fridgrinstance.c3qjm5y8runj.eu-west-2.rds.amazonaws.com"
    internal val port: String = "3306"

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
    private fun getResultSet(query: String): ResultSet? {
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
    private fun updateQuery(query: String) {
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
     * TODO: Actually do it x)
     */
    private fun hashPassword(password: String): String {
        return password
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

    override fun getUserPreferences(user_token: String): UserPreferences {
        this.connect()
        return TODO()
    }

    override fun writeUserPreferences(user_token: String, userPreferences: UserPreferences) {
        this.connect()
    }

    override fun getUserCuisines(user_token: String): List<Cuisine> {
        this.connect()
        return TODO()
    }

    override fun writeUserCuisines(user_token: String, userCuisines: List<Cuisine>) {
        connect()
    }
}