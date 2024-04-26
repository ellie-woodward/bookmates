package com.example.myapplication.data.model

// {'login_status': login_status}, status=200
data class LoginResponse (
    val message: Boolean,
    val status: Int
)