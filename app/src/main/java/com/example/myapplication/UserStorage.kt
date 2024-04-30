package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.model.User

class UserStorage(): Application() {

    companion object {
        var user: User = User("","", "", "")
    }
    fun getUser(): User{
        return user
    }
    fun setUser(currentuser: User){
        user = currentuser
    }

}