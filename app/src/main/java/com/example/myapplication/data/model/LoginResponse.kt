package com.example.myapplication.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONObject

// {'login_status': login_status}, status=200

@JsonClass(generateAdapter = true)
data class LoginResponse (
    @Json(name = "login_status") val status: Map<String, Boolean>,
    @Json(name = "status") val number: Int
)