package com.example.myapplication.data

import com.example.myapplication.data.model.User
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String) {
        try {
            // TODO: handle loggedInUser authentication

//            return Result.Success(fakeUser)
        } catch (e: Throwable) {
//            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}