package com.example.myapplication.data.model

import com.squareup.moshi.Json

data class CreatedPlayer(
    @Json(name = "_id") val id: Map<String, String>,
    @Json(name = "name") val name: String,
    @Json(name = "wins") val wins: Int,
    @Json(name = "games_total") val totalGames: Int,
    @Json(name= "game_records") val record: Map<String, Any>,
    @Json(name = "type") val type: String
){}

//Map<String, ArrayList<String>>