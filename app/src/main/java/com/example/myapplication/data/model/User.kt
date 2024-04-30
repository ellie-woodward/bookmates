package com.example.myapplication.data.model

import Player

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "_id") val userId: String,
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
    @Json(name = "email") val email: String,
    @Json(name = "created_player_list") val createdPlayers: List<CreatedPlayer>? = null,
    @Json(name = "games_logged") val gamesLogged: Int? = 0,
    @Json(name = "unique_games") val uniqueGames: Int? = 0,
    @Json(name = "templates_table") val templates: List<Template>? = null,
    @Json(name = "games_table") val games: List<Game>? = null

){}

@JsonClass(generateAdapter = true)
data class Account(
    @Json(name = "account_data") val accountData: User
){
}

