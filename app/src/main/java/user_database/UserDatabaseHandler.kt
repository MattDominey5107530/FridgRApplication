package user_database

import api.Cuisine
import com.example.fridgr.local_storage.UserPreferences
import java.sql.*
import java.util.*

const val DATABASE_USERNAME = ""
const val DATABASE_PASSWORD = ""

object UserDatabaseHandler : IUserDatabaseHandler{
    internal var conn: Connection? = null
    internal var username: String = DATABASE_USERNAME
    internal var password: String = DATABASE_PASSWORD

    fun connect() {
        val connectionProps = Properties().apply {
            put("user", username)
            put ("password", password)
        }

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/"
            )
        } catch (sqlE: SQLException) {
            //SQL exceptions
            sqlE.printStackTrace()
        } catch (e: Exception) {
            //Any other exceptions, (possible phone not connected to the internet)
            e.printStackTrace()
        }
    }

    private fun getResultSet(query: String): ResultSet? {
        requireNotNull(conn)

        var stmt: Statement? = null

        try {
            stmt = conn!!.createStatement()
            return stmt.executeQuery(query)
        } catch (sqlE: SQLException) {
            //SQL exceptions
            sqlE.printStackTrace()
        }
    }

    override fun authenticate(username: String, password: String): String? {
        TODO("Not yet implemented")
    }

    override fun getUserPreferences(user_token: String): UserPreferences {
        TODO("Not yet implemented")
    }

    override fun writeUserPreferences(user_token: String, userPreferences: UserPreferences) {
        TODO("Not yet implemented")
    }

    override fun getUserCuisines(user_token: String): List<Cuisine> {
        TODO("Not yet implemented")
    }

    override fun writeUserCuisines(user_token: String, userCuisines: List<Cuisine>) {
        TODO("Not yet implemented")
    }
}