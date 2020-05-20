package user_database

import android.util.Log
import api.Cuisine
import com.example.fridgr.local_storage.UserPreferences
import java.sql.*
import java.util.*

const val DATABASE_USERNAME = "root"
const val DATABASE_PASSWORD = "@Poppy@123@"

object UserDatabaseHandler : IUserDatabaseHandler {
    internal var conn: Connection? = null
    internal var username: String = DATABASE_USERNAME
    internal var password: String = DATABASE_PASSWORD

    fun connect() {
        val connectionProps = Properties()
        connectionProps.put("user", username)
        connectionProps.put("password", password)
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection(
                "jdbc:" + "mysql" + "://" +
                        "127.0.0.1" +
                        ":" + "3306" + "/" +
                        "",
                connectionProps)
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }

    //TODO: make private
    fun getResultSet(query: String): ResultSet? {
        requireNotNull(conn)

        var stmt: Statement? = null

        return try {
            stmt = conn!!.prepareStatement(query)
            stmt.executeQuery()
        } catch (sqlE: SQLException) {
            //SQL exceptions
            sqlE.printStackTrace()
            null
        }
    }

    //TODO: make private
    fun hashPassword(password: String): String {
        return password
    }

    //TODO: make private
    fun getRandomUserToken(): String {
        return java.util.UUID.randomUUID().toString()
    }

    override fun authenticate(username: String, password: String): String? {
        this.connect()
        val userToken: String = getRandomUserToken()
        val passwordHash: String = hashPassword(password)

        var query =
            "SELECT user_id FROM Users WHERE username='$username' AND password=$passwordHash;"
        var resultSet = getResultSet(query)

        if (resultSet != null && resultSet.fetchSize == 1) {
            query =
                "UPDATE Users SET user_token = '$userToken' WHERE username='$username' AND password=$passwordHash;"
            getResultSet(query)
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