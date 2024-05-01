package com.example.myapplication.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONArray
//{'error': 'Failed to create account'}, status=500

data class JsonResponse(
    val message: LinkedHashMap<String, String>,
    val status: Int
)


@JsonClass(generateAdapter = true)
data class CreateAccountResponse(
    @Json(name = "account_data") val user : User
)