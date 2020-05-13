package user_database

import com.example.fridgr.local_storage.UserPreferences

interface IUserDatabaseHandler {

    /**
     * Function which queries the database to find out whether the username and password is a valid
     *  combination. Should generate a random userToken string to pass to the database if this is
     *  a valid combination. This userToken MUST be unique within the database as it will be
     *  used to authenticate a user to get their UserPreferences.
     *  - Should return that userToken if valid.
     *  - Should return null if not
     *  E.g. username = "Matt"
     *       password = "helloworld123!"
     *       database -> valid!
     *       return "sd897sf090s90d8h9080dsf8s"
     *
     *  E.g.2 username = "Matt"
     *        password = "hellowrold123!"
     *        database -> invalid!
     *        return null
     */
    fun authenticate(username: String, password: String): String?

    /**
     * Function which should get the users preferences from the database using the userToken as
     *  authentication.
     */
    fun getUserPreferences(user_token: String): UserPreferences


}