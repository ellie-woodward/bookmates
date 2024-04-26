package com.example.myapplication.data.model

import Player
import org.json.JSONArray
import com.squareup.moshi.JsonClass

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@JsonClass(generateAdapter = true)
data class User(
    val userId: String,
    val username: String,
    val password: String,
    val email: String,
    val createdPlayers: List<Player>,
    val gamesLogged: Int,
    val uniqueGames: Int,
    val templates: List<Template>,
    val games: List<Game>
){}