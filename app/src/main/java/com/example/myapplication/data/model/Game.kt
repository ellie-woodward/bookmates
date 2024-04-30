package com.example.myapplication.data.model

import Player
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Game(
    @Json(name = "_id") val id: Map<String, String>,
    @Json(name = "match_name") val matchName: String,
    @Json(name = "game_name") val gameName: String,
    @Json(name = "template_id") val templateId: String,
    @Json(name = "is_finished") val finished: Boolean,
    @Json(name = "duration") val duration: Int,
    @Json(name = "scores_dict") val scores: Map<String, Int>,
    @Json(name = "players") val players: List<Player>,
    @Json(name = "winner") val winner: Player
){}




