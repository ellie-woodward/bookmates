package com.example.myapplication.data.model

import org.json.JSONArray
//{'error': 'Failed to create account'}, status=500
data class JsonResponse(
    val message: LinkedHashMap<String, String>,
    val status: Int
)