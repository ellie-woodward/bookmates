package com.example.myapplication.data

import com.example.myapplication.Player
import com.example.myapplication.data.api.BookMatesApi
import com.example.myapplication.data.model.User
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Retrofit
import retrofit2.create

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {



    // in-memory cache of the loggedInUser object
    private var user: User? = null


    val isLoggedIn: Boolean
        get() = user != null

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String) {
        // handle login
        val result = dataSource.login(username, password)

//        if (result is Result.Success)  {
//            setLoggedInUser(result.data)
//        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
    }
}